package com.project.trip.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.weather.model.OpenWeatherVo;

/**
 * OpenWeatherMap API를 호출하여 날씨 데이터를 조회하는 서비스 클래스입니다.
 * <p>
 * 특정 도시와 날짜에 대한 날씨 예보 정보를 가져오는 기능을 제공합니다.
 * </p>
 */
@Service
public class OpenWeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 특정 도시와 날짜에 대한 날씨 예보 정보를 OpenWeatherMap API로부터 가져옵니다.
     * <p>
     * API 응답에서 해당 날짜의 정오(12:00:00) 데이터를 우선적으로 선택하며,
     * 없으면 해당 날짜의 첫 번째 데이터를 반환합니다.
     * </p>
     * @param city 날씨 정보를 조회할 도시 이름
     * @param date 날씨 정보를 조회할 날짜 (yyyy-MM-dd 형식)
     * @return 조회된 날씨 정보를 담은 {@link OpenWeatherVo} 객체, 또는 조회 실패 시 null
     */
    public OpenWeatherVo getForecastByCityAndDate(String city, String date) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + "/forecast")
                    .queryParam("q", city)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .queryParam("lang", "kr")
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root.has("cod") && !"200".equals(root.get("cod").asText())) {
                System.out.println("[OpenWeather] error: " +
                        root.get("cod").asText() + " / " +
                        root.path("message").asText());
                return null;
            }

            JsonNode list = root.path("list");
            if (!list.isArray() || list.size() == 0) return null;

            String targetDate = date; // "yyyy-MM-dd"
            JsonNode chosen = null;

            for (JsonNode node : list) {
                String dtTxt = node.path("dt_txt").asText(); // "2025-11-14 12:00:00"
                if (dtTxt != null && dtTxt.startsWith(targetDate)) {
                    if (chosen == null) chosen = node;
                    if (dtTxt.endsWith("12:00:00")) {
                        chosen = node;
                        break;
                    }
                }
            }

            if (chosen == null) chosen = list.get(0);

            JsonNode mainNode = chosen.path("main");
            JsonNode weather0 = chosen.path("weather").get(0);

            OpenWeatherVo vo = new OpenWeatherVo();
            vo.setDateTime(chosen.path("dt_txt").asText());
            vo.setTemp(mainNode.path("temp").asDouble());
            vo.setMain(weather0.path("main").asText());
            vo.setDescription(weather0.path("description").asText());

            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

