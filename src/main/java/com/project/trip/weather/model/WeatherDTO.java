package com.project.trip.weather.model;

import lombok.Data;

@Data
public class WeatherDTO {

	private String main;
	private String description;
	private double temp;
	public String toPromptString() {
        return String.format(
                "날씨: %s, 설명: %s, 온도: %.1f°C",
                main,
                description,
                temp
        );
    }
}
