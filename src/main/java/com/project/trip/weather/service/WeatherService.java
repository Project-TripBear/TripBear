package com.project.trip.weather.service;

import com.project.trip.weather.model.WeatherDTO;

/**
 * 날씨 정보를 조회하는 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 특정 도시와 날짜에 대한 날씨 정보를 제공하는 기능을 담당합니다.
 * </p>
 */
public interface WeatherService {
	
	/**
	 * 특정 도시와 날짜에 대한 날씨 정보를 조회합니다.
	 *
	 * @param city 날씨 정보를 조회할 도시 이름
	 * @param date 날씨 정보를 조회할 날짜 (yyyy-MM-dd 형식)
	 * @return 조회된 날씨 정보를 담은 {@link WeatherDTO} 객체
	 */
	WeatherDTO getWeather(String city, String date);

}
