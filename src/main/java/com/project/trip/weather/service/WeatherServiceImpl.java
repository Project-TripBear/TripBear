package com.project.trip.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.weather.model.OpenWeatherVo;
import com.project.trip.weather.model.WeatherDTO;

@Service
public class WeatherServiceImpl implements WeatherService{
	
	@Autowired
    private OpenWeatherService openWeatherService;

    @Override
    public WeatherDTO getWeather(String city, String date) {

        // 1. OpenWeather에서 예보 한 건 가져오기
        OpenWeatherVo vo = openWeatherService.getForecastByCityAndDate(city, date);

        // 2. 예외 처리: API 실패 시 기본값 리턴 (AI 프롬프트가 완전 비지 않게)
        if (vo == null) {
            System.out.println("[WeatherService] OpenWeather 호출 실패, 기본값 사용");
            WeatherDTO fallback = new WeatherDTO();
            fallback.setMain("Clear");
            fallback.setDescription("날씨 정보를 가져오지 못해 기본값을 사용합니다.");
            fallback.setTemp(20.0);
            return fallback;
        }

        // 3. OpenWeatherVo → WeatherDTO 변환
        WeatherDTO dto = new WeatherDTO();
        dto.setMain(vo.getMain());
        dto.setDescription(vo.getDescription());
        dto.setTemp(vo.getTemp());

        System.out.println("[WeatherService] OpenWeather 결과: "
                + dto.getMain() + " / " + dto.getTemp() + "°C / " + dto.getDescription());

        return dto;
    }
}
