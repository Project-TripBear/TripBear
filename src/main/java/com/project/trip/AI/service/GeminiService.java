package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.weather.model.WeatherDTO;

/**
 * Google Gemini AI를 활용하여 여행 경로를 생성하는 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface GeminiService {

	/**
	 * 사용자 선호도와 날씨 정보를 기반으로 Gemini AI를 통해 여행 경로를 생성합니다.
	 * @param dto 사용자 선호도 정보가 담긴 {@link AiRouteRequestDTO} 객체
	 * @param weather 현재 날씨 정보가 담긴 {@link WeatherDTO} 객체
	 * @return 생성된 AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
	 */
	RouteDTO generateRoute(AiRouteRequestDTO dto, WeatherDTO weather);

}
