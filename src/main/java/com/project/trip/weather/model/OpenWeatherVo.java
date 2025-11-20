package com.project.trip.weather.model;

import lombok.Data;

/**
 * OpenWeatherMap API 응답에서 필요한 날씨 정보를 간략하게 담는 값 객체(VO)입니다.
 * <p>
 * 주로 날짜/시간, 온도, 주요 날씨 상태, 상세 설명을 포함합니다.
 * </p>
 */
@Data
public class OpenWeatherVo {

	/**
	 * 날짜 및 시간 (예: "2025-11-18 10:00:00")
	 */
	private String dateTime;
	/**
	 * 온도 (섭씨)
	 */
	private double temp;
	/**
	 * 주요 날씨 상태 (예: "Clouds", "Clear")
	 */
	private String main;
	/**
	 * 날씨 상세 설명 (예: "scattered clouds", "clear sky")
	 */
	private String description;
	
}
