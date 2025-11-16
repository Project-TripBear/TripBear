package com.project.trip.allplace.service;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // [추가]
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode; // [추가]
import com.fasterxml.jackson.databind.ObjectMapper; // [추가]
import com.project.trip.allplace.model.WeatherVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Service("krWeatherService") // Bean 이름("krWeatherService") 유지
public class KrWeatherServiceImpl implements KrWeatherService { // 기존 인터페이스 구현

    @Autowired
    private RestTemplate restTemplate;

    // --- [수정] OpenWeatherMap API 정보 ---
    // application.properties에 키와 URL이 설정되어 있어야 합니다.
    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String baseUrl; // 예: https://api.openweathermap.org/data/2.5

    private final ObjectMapper objectMapper = new ObjectMapper();
    // --- [여기까지 수정] ---

    @Override
    public WeatherVO getTodayWeather(double lat, double lon) {
        return getTodayWeather(String.valueOf(lat), String.valueOf(lon));
    }

    /**
     * [로직 변경] OpenWeatherMap 'Current Weather' API를 호출합니다.
     */
    @Override
    public WeatherVO getTodayWeather(String lat, String lon) {
        
        log.info("[Weather] OpenWeatherMap API 요청: lat=" + lat + ", lon=" + lon);
        
        if (apiKey == null || baseUrl == null) {
            log.error("[Weather] OpenWeatherMap API Key 또는 URL이 설정되지 않았습니다.");
            return null;
        }

        try {
            // 1. OpenWeatherMap '현재 날씨' API 호출
            JsonNode root = this.callOpenWeatherApi(lat, lon);

            if (root == null) {
                log.warn("[Weather] OpenWeatherMap API 응답이 비어있습니다.");
                return null;
            }
            
            // 2. OpenWeatherMap JSON을 기존 WeatherVO 객체로 변환
            return this.processOpenWeatherResponse(root);

        } catch (Exception e) {
            log.error("[Weather] OpenWeatherMap API 호출/가공 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * [신규] 위도/경도로 '현재 날씨' (/weather) API를 호출합니다.
     */
    private JsonNode callOpenWeatherApi(String lat, String lon) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(this.baseUrl + "/weather") // '/weather' (현재 날씨) 엔드포인트
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("appid", this.apiKey)
                    .queryParam("units", "metric") // 섭씨 온도
                    .queryParam("lang", "kr") // 한국어 설명
                    .build(false) // 인코딩 수행
                    .toUri();
            
            log.info("[Weather] OpenWeatherMap 요청 URI: " + uri);

            String response = restTemplate.getForObject(uri, String.class);
            JsonNode root = objectMapper.readTree(response);

            // API 자체 에러 체크 (예: 401, 404)
            if (root.has("cod") && !"200".equals(root.get("cod").asText())) {
                log.error("[Weather] OpenWeatherMap API 오류: " +
                        root.get("cod").asText() + " / " +
                        root.path("message").asText());
                return null;
            }
            return root;
            
        } catch (Exception e) {
            log.error("[Weather] OpenWeatherMap API 호출 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * [신규] OpenWeatherMap 'Current' API (JsonNode)를
     * 프론트엔드 'WeatherVO'로 가공 (Adapter 역할)
     */
    private WeatherVO processOpenWeatherResponse(JsonNode root) {
        WeatherVO vo = new WeatherVO();

        // 1. 시간 변환 (Unix timestamp -> yyyyMMdd / HHmm)
        long dt = root.path("dt").asLong();
        Instant instant = Instant.ofEpochSecond(dt);
        ZoneId zone = ZoneId.of("Asia/Seoul"); // 한국 시간대
        vo.setBaseDate(DateTimeFormatter.ofPattern("yyyyMMdd").withZone(zone).format(instant));
        vo.setBaseTime(DateTimeFormatter.ofPattern("HHmm").withZone(zone).format(instant));

        // 2. 핵심 날씨 정보 (main 객체)
        JsonNode main = root.path("main");
        vo.setTemperature(main.path("temp").asText()); // 예: "13.2"
        vo.setHumidity(main.path("humidity").asText()); // 예: "60"

        // 3. 하늘 상태 (weather 배열)
        String skyDescription = "정보 없음";
        String skyApiCode = "0"; // KMA와 다르지만 OWM ID 저장
        JsonNode weatherArray = root.path("weather");
        
        if (weatherArray.isArray() && weatherArray.size() > 0) {
            JsonNode weather = weatherArray.get(0);
            skyDescription = weather.path("description").asText(); // 예: "약간의 구름"
            skyApiCode = weather.path("id").asText(); // 예: "801"
        }
        vo.setSkyStatus(skyDescription);
        vo.setSkyCode(skyApiCode);

        // 4. 강수량 (Rain)
        String rainAmount = "강수없음";
        String rainType = "없음";
        String ptyCode = "0"; // 0: 없음 (KMA 기준)
        
        JsonNode rain = root.path("rain");
        if (rain.has("1h")) { // 1시간 강수량
            rainAmount = rain.path("1h").asText() + "mm";
            rainType = "비";
            ptyCode = "1"; // 1: 비 (KMA 기준)
            vo.setSkyStatus("비"); // 하늘 상태 덮어쓰기
        }

        // 5. 강설량 (Snow)
        JsonNode snow = root.path("snow");
        if (snow.has("1h")) { // 1시간 강설량
            rainAmount = snow.path("1h").asText() + "mm";
            rainType = "눈";
            ptyCode = "3"; // 3: 눈 (KMA 기준)
            vo.setSkyStatus("눈"); // 하늘 상태 덮어쓰기
        }
        
        vo.setRainAmount(rainAmount);
        vo.setRainType(rainType);
        vo.setPtyCode(ptyCode);
        
        log.info("[Weather] OpenWeatherMap 가공 완료: " + vo.getSkyStatus() + ", " + vo.getTemperature() + "°C");
        return vo;
    }
    
    // [삭제] KMA 관련 헬퍼 메서드 (GpsConverter, KmaResponseVO, getBaseDateTime, decodeSky 등) 모두 제거
}