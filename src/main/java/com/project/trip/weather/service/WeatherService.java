package com.project.trip.weather.service;

import com.project.trip.weather.model.WeatherDTO;

public interface WeatherService {
	
	WeatherDTO getWeather(String city, String date);

}
