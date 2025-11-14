package com.project.trip.allplace.service;

import com.project.trip.allplace.model.WeatherVO;

public interface KrWeatherService {

    /**
     * 위도(lat)와 경도(lon)를 기준으로
     * 기상청 단기예보 API를 호출하여 현재 날씨 정보를 가져옵니다.
     * * @param lat 위도 (e.g., "37.579043")
     * @param lon 경도 (e.g., "126.974055")
     * @return 가공된 날씨 정보 (WeatherVO)
     */
    public WeatherVO getTodayWeather(String lat, String lon);
    
    /**
     * [오버로딩] 위도(lat)와 경도(lon)를 double로 받습니다.
     */
    public WeatherVO getTodayWeather(double lat, double lon);
}