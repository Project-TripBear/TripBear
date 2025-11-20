package com.project.trip.weather.mapper;

import com.project.trip.weather.model.WeatherDataDTO;

/**
 * 날씨 데이터와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 날씨 데이터를 데이터베이스에 삽입하는 기능을 정의합니다.
 * </p>
 */
public interface WeatherMapper {

    /**
     * 새로운 날씨 데이터를 데이터베이스에 삽입합니다.
     *
     * @param dto 삽입할 날씨 데이터를 담은 {@link WeatherDataDTO} 객체
     */
    void insertWeatherData(WeatherDataDTO dto);
}