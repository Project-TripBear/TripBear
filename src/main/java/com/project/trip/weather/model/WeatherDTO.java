package com.project.trip.weather.model;

import lombok.Data;

/**
 * 날씨 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 주요 날씨 상태, 상세 설명, 온도를 포함하며, 프롬프트 문자열로 변환하는 기능을 제공합니다.
 * </p>
 */
@Data
public class WeatherDTO {

	/**
	 * 주요 날씨 상태 (예: "Clouds", "Clear")
	 */
	private String main;
	/**
	 * 날씨 상세 설명 (예: "scattered clouds", "clear sky")
	 */
	private String description;
	/**
	 * 온도 (섭씨)
	 */
	private double temp;
	/**
	 * 현재 날씨 정보를 사람이 읽기 쉬운 프롬프트 문자열 형식으로 변환합니다.
	 *
	 * @return "날씨: [main], 설명: [description], 온도: [temp]°C" 형식의 문자열
	 */
	public String toPromptString() {
        return String.format(
                "날씨: %s, 설명: %s, 온도: %.1f°C",
                main,
                description,
                temp
        );
    }
}
