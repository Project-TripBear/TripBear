package com.project.trip.weather.model;

import java.util.Date;

import lombok.Data;

/**
 * 날씨 데이터를 나타내는 데이터 전송 객체(DTO)입니다.
 * <p>
 * `tblWeatherData` 테이블과 매핑되며, 날씨 정보와 그에 따른 추천 타입 및 코멘트를 포함합니다.
 * </p>
 */
@Data
public class WeatherDataDTO {

    /**
     * 날씨 데이터 고유 ID (PK)
     */
    private Long weatherId;
    /**
     * 여행 날짜
     */
    private Date travelDate;
    /**
     * 도시 이름
     */
    private String cityName;
    /**
     * 온도 (섭씨)
     */
    private Double temp;
    /**
     * 주요 날씨 상태 (예: "Clouds", "Clear")
     */
    private String weatherMain;
    /**
     * 날씨 상세 설명 (예: "scattered clouds", "clear sky")
     */
    private String weatherDesc;
    /**
     * 추천 타입 (예: "INDOOR", "OUTDOOR", "FOLIAGE", "NORMAL")
     */
    private String recommendType;   // INDOOR / OUTDOOR / FOLIAGE / NORMAL
    /**
     * 날씨 조언 코멘트 (팝업 문구)
     */
    private String weatherComment;  // 팝업 문구
}