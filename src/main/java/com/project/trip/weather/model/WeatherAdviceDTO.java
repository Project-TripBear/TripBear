package com.project.trip.weather.model;

import lombok.Data;

/**
 * 날씨 기반 여행 조언 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 추천 타입, 메시지, 온도, 주요 날씨 상태, 상세 설명 등을 포함합니다.
 * </p>
 */
@Data
public class WeatherAdviceDTO {
    
    /**
     * 추천 타입 (예: "활동적", "실내")
     */
    private String recommendType;
    /**
     * 날씨 조언 메시지
     */
    private String message;
    /**
     * 온도 (섭씨)
     */
    private Double temp;
    /**
     * 주요 날씨 상태 (예: "Clouds", "Clear")
     */
    private String main;
    /**
     * 날씨 상세 설명 (예: "scattered clouds", "clear sky")
     */
    private String description;
    
}