package com.project.trip.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.weather.model.OpenWeatherVo;
import com.project.trip.weather.model.WeatherDTO;

/**
 * {@link WeatherService} 인터페이스의 구현 클래스입니다.
 * <p>
 * OpenWeatherMap API를 통해 날씨 정보를 가져오고, 이를 가공하여 제공하는 비즈니스 로직을 처리합니다.
 * 도시 이름 정규화 및 API 호출 실패 시 기본값 처리 로직을 포함합니다.
 * </p>
 */
@Service
public class WeatherServiceImpl implements WeatherService{
    
    @Autowired
    private OpenWeatherService openWeatherService;

    /**
     * 한글 도시 이름을 OpenWeatherMap API에서 사용하는 영문 도시 이름으로 정규화합니다.
     * <p>
     * 지원하는 도시 외의 이름은 그대로 반환합니다.
     * </p>
     * @param city 정규화할 도시 이름 (한글 또는 영문)
     * @return 정규화된 영문 도시 이름
     */
    private String normalizeCity(String city) {
        if (city == null) return null;
        switch (city) {
            case "서울": return "Seoul";
            case "제주": return "Jeju";
            case "부산": return "Busan";
            case "강릉": return "Gangneung";
            case "전주": return "Jeonju";
            case "경주": return "Gyeongju";
            case "인천": return "Incheon";
            case "대구": return "Daegu";
            case "대전": return "Daejeon";
            case "춘천": return "Chuncheon";
            default: return city; // 이미 영어면 그대로
        }
    }

    
    @Override
    public WeatherDTO getWeather(String city, String date) {

        String normalizedCity = normalizeCity(city);
        
        System.out.println("[WeatherService] 요청 city=" + city + "→ API city=" + normalizedCity);
        
        
        // 1. OpenWeather에서 예보 한 건 가져오기
        OpenWeatherVo vo = openWeatherService.getForecastByCityAndDate(normalizedCity, date);

        
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