package com.project.trip.weather.model;

import lombok.Data;

@Data
public class OpenWeatherVo {

	private String dateTime;
	private double temp;
	private String main;
	private String description;
	
}
