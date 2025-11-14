package com.project.trip.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trip.AI.model.WeatherDTO;

@Service
public class WeatherServiceImpl implements WeatherService{
	
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${openweather.api.key}")
	private String apiKey;
	
	@Value("${openwather.api.url}")
	private String baseUrl;
	
	public WeatherServiceImpl (RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public WeatherDTO getWeather(String city, String date) {

		try {
			String url = UriComponentsBuilder
						.fromHttpUrl(baseUrl + "/forecast")
						.queryParam("q", city)
						.queryParam("appid", apiKey)
						.queryParam("units", "metric")
						.build()
						.toUriString();
			
			String response = restTemplate.getForObject(url, String.class);
			System.out.println("[WeatherService] OpenWeather forecast response = " + response);
			
			JsonNode root = objectMapper.readTree(response);
			
			if (root.has("cod") && !"200".equals(root.get("cod").asText())) {
				System.out.println("[WeatherService] OpenWeather erro code = " + root.get("cod").asText());
				return null;
			}
			
			JsonNode list = root.path("list");
			
			if(!list.isArray() || list.size() == 0) {
				System.out.println("[WeatherService] forecast list is empty");
				return null;
			}
			
			 JsonNode chosen = null;
	
	         for (JsonNode node : list) {
	             String dtTxt = node.path("dt_txt").asText(); // 예: "2025-12-24 12:00:00"
	
	             if (dtTxt != null && dtTxt.startsWith(date)) {
	                 // 같은 날짜라면 우선 후보로
	                 if (chosen == null) {
	                     chosen = node;
	                 }
	                 // 12시 예보면 그냥 확정
	                 if (dtTxt.contains("12:00:00")) {
	                     chosen = node;
	                     break;
	                 }
	             }
	         }
	
	         // 해당 날짜 예보가 없으면 첫 번째 예보라도 반환
	         if (chosen == null) {
	             chosen = list.get(0);
	         }
	
	         //선택된 예보에서 temp / main / description 추출
	         JsonNode mainNode = chosen.path("main");
	         JsonNode weather0 = chosen.path("weather").get(0);
	
	         double temp = mainNode.path("temp").asDouble();
	         String main = weather0.path("main").asText();           // Rain, Snow, Clear 등
	         String description = weather0.path("description").asText(); // light snow, heavy intensity rain 등
	
	         WeatherDTO dto = new WeatherDTO();
	         dto.setTemp(temp);
	         dto.setMain(main);
	         dto.setDescription(description);
	
	         return dto;

	     } catch (Exception e) {
	         e.printStackTrace();
	         return null; // 필요하면 예외 던지도록 바꿔도 됨
	     }
	}
}
