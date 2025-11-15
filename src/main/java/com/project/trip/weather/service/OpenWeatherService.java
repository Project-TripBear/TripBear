package com.project.trip.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.weather.model.OpenWeatherVo;

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

