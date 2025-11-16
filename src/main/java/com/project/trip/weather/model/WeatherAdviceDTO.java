package com.project.trip.weather.model;

import lombok.Data;

@Data
public class WeatherAdviceDTO {
    
    private String recommendType;
    private String message;
    private Double temp;
    private String main;
    private String description;
    
}