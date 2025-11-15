package com.project.trip.weather.model;

import java.util.Date;

import lombok.Data;

@Data
public class WeatherDataDTO {

    private Long weatherId;
    private Date travelDate;
    private String cityName;
    private Double temp;
    private String weatherMain;
    private String weatherDesc;
    private String recommendType;   // INDOOR / OUTDOOR / FOLIAGE / NORMAL
    private String weatherComment;  // 팝업 문구
}