package com.project.trip.allplace.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.trip.allplace.model.WeatherVO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    private final String serviceKey = "4ad9f6404c1b5c50ee33409214a285bd720eab16c791577e2e460245c5f3b7b4";
    
    // --- [수정] API URL을 '초단기예보'로 변경 ---
    private final String KMA_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

    @Override
    public WeatherVO getTodayWeather(double lat, double lon) {
        return getTodayWeather(String.valueOf(lat), String.valueOf(lon));
    }

    @Override
    public WeatherVO getTodayWeather(String lat, String lon) {
        
        // 1. 요청 시각/날짜 계산
        Map<String, String> baseDateTime = getBaseDateTime();
        String baseDate = baseDateTime.get("baseDate"); // "20251112"
        String baseTime = baseDateTime.get("baseTime"); // "0930" (초단기)

        // 2. 위도/경도 -> 기상청 격자 X/Y 좌표로 변환
        GpsConverter converter = new GpsConverter();
        GpsConverter.LatXLngY grid = converter.convertGRID_GPS(0, Double.parseDouble(lat), Double.parseDouble(lon));
        
        String nx = String.valueOf((int)grid.x);
        String ny = String.valueOf((int)grid.y);

        log.info("[Weather] 기상청(초단기) API 요청: " + baseDate + "/" + baseTime + " (nx=" + nx + ", ny=" + ny + ")");

        // 3. API URI 빌드
        URI uri = UriComponentsBuilder
                .fromHttpUrl(KMA_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "100") 
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build(true) 
                .toUri();
        
        try {
            // 4. API 호출 및 파싱
            KmaResponseVO response = restTemplate.getForObject(uri, KmaResponseVO.class);

            if (response == null || response.getResponse() == null || response.getResponse().getBody() == null || response.getResponse().getBody().getItems() == null) {
                log.warn("[Weather] 기상청(초단기) API 응답이 비어있습니다. (baseTime: " + baseTime + ")");
                return null;
            }
            
            // 5. 파싱된 데이터를 WeatherVO로 가공
            return processKmaResponse(response.getResponse().getBody().getItems().getItem());

        } catch (Exception e) {
            log.error("[Weather] 기상청(초단기) API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * API가 반환한 Item 리스트를 프론트용 WeatherVO 1개로 가공
     */
    private WeatherVO processKmaResponse(List<KmaResponseVO.Item> items) {
        WeatherVO vo = new WeatherVO();
        String fcstTime = null; // (가장 빠른 예보 시각)
        
        // (T1H(기온), SKY(하늘), PTY(강수), RN1(강수량), REH(습도)만 필터링)
        for (KmaResponseVO.Item item : items) {
            
            // 가장 빠른 예보 시각을 찾기
            if (fcstTime == null) {
                fcstTime = item.getFcstTime();
                vo.setBaseDate(item.getBaseDate());
                vo.setBaseTime(item.getBaseTime());
            }
            
            // 가장 빠른 시각의 데이터만 사용
            if (!item.getFcstTime().equals(fcstTime)) {
                continue; 
            }
            
            String category = item.getCategory();
            String value = item.getFcstValue();
            
            switch (category) {
                case "T1H": // 1시간 기온
                    vo.setTemperature(value);
                    break;
                case "SKY": // 하늘 상태
                    vo.setSkyCode(value);
                    vo.setSkyStatus(decodeSky(value));
                    break;
                case "PTY": // 강수 형태
                    vo.setPtyCode(value);
                    vo.setRainType(decodePty(value));
                    break;
                case "RN1": // 1시간 강수량
                    vo.setRainAmount(decodeRn1(value)); // [수정] POP -> RN1
                    break;
                case "REH": // 습도
                    vo.setHumidity(value);
                    break;
            }
        }
        
        // [중요] '하늘 상태'와 '강수 형태' 보정
        if (!"0".equals(vo.getPtyCode())) { // (0 = 강수 없음)
            vo.setSkyStatus(vo.getRainType());
        }
        
        return vo;
    }
    
    /**
     * [수정] '초단기예보' API의 baseTime 계산 로직
     * - API는 매시 30분에 데이터를 생성하며, 45분에 API 제공
     */
    private Map<String, String> getBaseDateTime() {
        Map<String, String> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now(); // 예: 10:15
        
        // 45분 미만일 경우, 1시간 전 데이터를 사용
        // 예: 10:15 -> 09:30 데이터를 요청
        // 예: 10:50 -> 10:30 데이터를 요청
        LocalDateTime baseDateTime;
        
        if (now.getMinute() < 45) {
            baseDateTime = now.minusHours(1); // 1시간 전
        } else {
            baseDateTime = now; // 현재 시간
        }

        result.put("baseDate", baseDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        result.put("baseTime", baseDateTime.format(DateTimeFormatter.ofPattern("HH30"))); // "0930"
        
        return result;
    }

    // --- 기상청 코드 변환 헬퍼 ---
    
    // (SKY) 하늘상태: 1(맑음), 3(구름많음), 4(흐림)
    private String decodeSky(String skyCode) {
        if (skyCode == null) return "정보 없음";
        switch (skyCode) {
            case "1": return "맑음";
            case "3": return "구름많음";
            case "4": return "흐림";
            default: return "정보 없음";
        }
    }

    // (PTY) 강수형태: 0(없음), 1(비), 2(비/눈), 3(눈), 5(빗방울), 6(빗방울/눈날림), 7(눈날림)
    private String decodePty(String ptyCode) {
        if (ptyCode == null) return "정보 없음";
        switch (ptyCode) {
            case "0": return "없음";
            case "1": return "비";
            case "2": return "비/눈";
            case "3": return "눈";
            case "5": return "빗방울";
            case "6": return "빗방울/눈날림";
            case "7": return "눈날림";
            default: return "정보 없음";
        }
    }
    
    // --- [신규] ---
    // (RN1) 1시간 강수량 (mm)
    private String decodeRn1(String rn1) {
        if (rn1 == null) return "-";
        
        try {
            double amount = Double.parseDouble(rn1);
            if (amount == 0.0) {
                return "강수없음";
            } else if (amount > 0.0 && amount < 1.0) {
                return "1mm 미만";
            } else if (amount >= 1.0 && amount < 30.0) {
                return String.format("%.0fmm", amount); // "1mm", "5mm"
            } else if (amount >= 30.0) {
                return "30mm 이상";
            }
        } catch (NumberFormatException e) {
            // "강수없음" 등의 문자열이 올 경우
            if ("강수없음".equals(rn1)) return rn1;
            return "-";
        }
        return "-";
    }
    
    // ------------------------------------------
    // 기상청 API 응답을 받기 위한 중첩 VO (기존과 동일)
    // ------------------------------------------
    @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KmaResponseVO {
        private Response response;
        
        @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Response { private Body body; }

        @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Body { private Items items; }

        @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Items { private List<Item> item; }

        @Data @NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            @JsonProperty("baseDate")
            private String baseDate; 
            @JsonProperty("baseTime")
            private String baseTime; 
            @JsonProperty("category")
            private String category; 
            @JsonProperty("fcstDate")
            private String fcstDate; 
            @JsonProperty("fcstTime")
            private String fcstTime; 
            @JsonProperty("fcstValue")
            private String fcstValue; 
            @JsonProperty("nx")
            private int nx;
            @JsonProperty("ny")
            private int ny;
        }
    }

    // ------------------------------------------
    // (표준) 위도/경도 <-> 기상청 격자 X/Y 변환기 (기존과 동일)
    // ------------------------------------------
    public class GpsConverter {
        
        @Data
        public class LatXLngY {
            public double lat;
            public double lng;
            public double x;
            public double y;
        }

        private static final double RE = 6371.00877; // 지구 반경(km)
        // (이하 GpsConverter 코드는 기존과 동일)
        private static final double GRID = 5.0;      
        private static final double SLAT1 = 30.0;    
        private static final double SLAT2 = 60.0;    
        private static final double OLON = 126.0;    
        private static final double OLAT = 38.0;     
        private static final double XO = 43;         
        private static final double YO = 136;        

        public LatXLngY convertGRID_GPS(int mode, double lat, double lon) {
            double DEGRAD = Math.PI / 180.0;
            double RADDEG = 180.0 / Math.PI;

            double re = RE / GRID;
            double slat1 = SLAT1 * DEGRAD;
            double slat2 = SLAT2 * DEGRAD;
            double olon = OLON * DEGRAD;
            double olat = OLAT * DEGRAD;

            double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
            double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
            double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);
            LatXLngY rs = new LatXLngY();

            if (mode == 0) {
                rs.lat = lat;
                rs.lng = lon;
                double ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
                ra = re * sf / Math.pow(ra, sn);
                double theta = lon * DEGRAD - olon;
                if (theta > Math.PI) theta -= 2.0 * Math.PI;
                if (theta < -Math.PI) theta += 2.0 * Math.PI;
                theta *= sn;
                rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
                rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
            } else {
                rs.x = lat;
                rs.y = lon;
                double xn = lat - XO;
                double yn = ro - lon + YO;
                double ra = Math.sqrt(xn * xn + yn * yn);
                if (sn < 0.0) ra = -ra;
                double alat = Math.pow((re * sf / ra), (1.0 / sn));
                alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

                double theta;
                if (Math.abs(xn) <= 0.0) {
                    theta = 0.0;
                } else {
                    if (Math.abs(yn) <= 0.0) {
                        theta = Math.PI * 0.5;
                        if (xn < 0.0) theta = -theta;
                    } else theta = Math.atan2(xn, yn);
                }
                double alon = theta / sn + olon;
                rs.lat = alat * RADDEG;
                rs.lng = alon * RADDEG;
            }
            return rs;
        }
    }
}