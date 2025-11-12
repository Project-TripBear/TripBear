package com.project.trip.AI.service;

import com.project.trip.AI.model.WeatherDTO;

public interface WeatherService {
	
	WeatherDTO getWeather(String city, String date);

}
