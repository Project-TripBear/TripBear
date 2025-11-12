package com.project.trip.AI.service;

import org.springframework.stereotype.Service;

import com.project.trip.AI.model.WeatherDTO;

@Service
public class WeatherServiceImpl implements WeatherService{

	@Override
	public WeatherDTO getWeather(String city, String date) {

		System.out.println("[WeatherService] 날씨 API 호출(시물레이션)");
		WeatherDTO dummyWeather = new WeatherDTO();
		if (city.equals("제주")) {
			dummyWeather.setMain("Rain");
            dummyWeather.setDescription("약한 비");
            dummyWeather.setTemp(15.5);
        } else {
            dummyWeather.setMain("Clear");
            dummyWeather.setDescription("맑음");
            dummyWeather.setTemp(18.0);
        }
        return dummyWeather;
	}
	

}
