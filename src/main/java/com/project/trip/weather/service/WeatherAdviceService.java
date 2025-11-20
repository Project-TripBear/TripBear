package com.project.trip.weather.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.weather.mapper.WeatherMapper;
import com.project.trip.weather.model.WeatherAdviceDTO;
import com.project.trip.weather.model.WeatherDTO;
import com.project.trip.weather.model.WeatherDataDTO;

/**
 * 날씨 정보를 기반으로 여행 조언을 제공하고 이를 저장하는 서비스 클래스입니다.
 * <p>
 * 단기 예보(5일 이내)와 계절별 날씨 특성을 고려하여 여행 추천 타입을 결정하고,
 * 관련 메시지를 생성하여 반환합니다.
 * </p>
 */
@Service
public class WeatherAdviceService {

    @Autowired
    private WeatherService weatherService;   // 이미 OpenWeather 연동된 구현

    @Autowired
    private WeatherMapper weatherMapper;

    /**
     * 특정 도시와 날짜에 대한 날씨 조언을 생성하고 데이터베이스에 저장합니다.
     * <p>
     * 현재 날짜로부터 5일 이내의 날짜는 단기 예보를 기반으로 조언을 생성하고,
     * 그 외의 날짜는 계절별 특성을 고려한 조언을 생성합니다.
     * </p>
     * @param city 날씨 조언을 생성할 도시 이름 (예: "Seoul")
     * @param date 날씨 조언을 생성할 날짜 (yyyy-MM-dd 형식 문자열)
     * @return 생성된 날씨 조언 정보를 담은 {@link WeatherAdviceDTO} 객체
     */
    public WeatherAdviceDTO getAdviceAndSave(String city, String date) {

        LocalDate travelDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        long dayDiff = ChronoUnit.DAYS.between(today, travelDate);
        
        WeatherAdviceDTO advice;
        
        if (dayDiff >= 0 && dayDiff <= 5) {
            advice = buildShortTermAdvice(city, date);
        }
        else {
            advice = buildSeasonalAdvice(city, date);
        }
        
        if (advice == null) {
            advice = new WeatherAdviceDTO();
            advice.setRecommendType("NONE");
            advice.setMessage("날씨 정보를 가져오지 못했어요. 기본 코스로 추천할게요.");
            return advice;
        }

        if (!"NONE".equals(advice.getRecommendType())) {
            
            WeatherDataDTO dto = new WeatherDataDTO();
            dto.setTravelDate(Date.valueOf(date));
            dto.setCityName(city);
            dto.setTemp(advice.getTemp());
            dto.setWeatherMain(advice.getMain());
            dto.setWeatherDesc(advice.getDescription());
            dto.setRecommendType(advice.getRecommendType());
            dto.setWeatherComment(advice.getMessage());
            
            weatherMapper.insertWeatherData(dto);
        }
        
        return advice;
    }
    
   
    /**
     * 단기 예보(오늘부터 5일 이내)를 기반으로 날씨 조언을 생성합니다.
     * <p>
     * 실제 날씨 데이터를 조회하여 비, 눈, 단풍 시즌 등을 고려한 추천 타입과 메시지를 반환합니다.
     * </p>
     * @param city 날씨 조언을 생성할 도시 이름
     * @param date 날씨 조언을 생성할 날짜 (yyyy-MM-dd 형식 문자열)
     * @return 단기 날씨 조언 정보를 담은 {@link WeatherAdviceDTO} 객체
     */
    private WeatherAdviceDTO buildShortTermAdvice(String city, String date) {
        
        WeatherDTO weather = weatherService.getWeather(city, date);
        WeatherAdviceDTO advice = new WeatherAdviceDTO();
        
        if (weather == null ||
                "날씨 정보를 가져오지 못해 기본값을 사용합니다."
                    .equals(weather.getDescription())) {
            
            advice.setRecommendType("NONE");
            advice.setMessage("날씨 정보를 가져오지 못했어요. 기본 코스로 추천할게요.");
            return advice;
        }
        
        advice.setTemp(weather.getTemp());
        advice.setMain(weather.getMain());
        advice.setDescription(weather.getDescription());
        
        String mainLower = weather.getMain() != null
                ? weather.getMain().toLowerCase() : "";
        String descLower = weather.getDescription() != null
                ? weather.getDescription().toLowerCase() : "";
        
        boolean rain = mainLower.contains("rain") || mainLower.contains("drizzle");
        boolean snow = mainLower.contains("snow");
        boolean heavyRain = descLower.contains("heavy") || descLower.contains("storm");
        boolean lightRain = rain && !heavyRain;
        
        int month = Integer.parseInt(date.substring(5,7));
        boolean autumn = (month == 10 || month == 11);
        
        String recommendType;
        String comment;
        
        if ((rain || snow) && heavyRain) {
            recommendType = "INDOOR";
            comment = "폭우/폭설 예보가 있어서 실내 위주로 여행하는 걸 추천드려요.";
        } 
        else if (autumn && lightRain) {
            recommendType = "FOLIAGE";
            comment = "가벼운 비가 있지만 단풍 시즌이라 야외 단풍 코스도 괜찮아요 🍁";
        }
        else if (rain || snow) {
            recommendType = "INDOOR";
            comment = "비나 눈 예보가 있어서 실내 코스를 중심으로 잡는 게 좋아 보여요.";
        }
        else if (autumn) {
            recommendType = "FOLIAGE";
            comment = "단풍 시즌이라 야외 단풍 코스를 추천드려요 🍁";
        }
        else {
            recommendType = "OUTDOOR";
            comment = "큰 비/눈 예보가 없어서 야외 코스를 즐기기 좋은 날씨예요.";
        }

        advice.setRecommendType(recommendType);
        advice.setMessage(comment);

        return advice;
        
        
    }

    /**
     * 계절별 특성을 기반으로 날씨 조언을 생성합니다.
     * <p>
     * 단기 예보 기간을 벗어나는 날짜에 대해 계절(단풍 시즌, 장마철, 한겨울 등)에 따른
     * 일반적인 여행 추천 타입과 메시지를 반환합니다.
     * </p>
     * @param city 날씨 조언을 생성할 도시 이름 (현재는 사용되지 않음)
     * @param date 날씨 조언을 생성할 날짜 (yyyy-MM-dd 형식 문자열)
     * @return 계절별 날씨 조언 정보를 담은 {@link WeatherAdviceDTO} 객체
     */
    private WeatherAdviceDTO buildSeasonalAdvice(String city, String date) {
        
        WeatherAdviceDTO advice = new WeatherAdviceDTO();

        int month = Integer.parseInt(date.substring(5, 7));

        String recommendType;
        String comment;
        
        if (month == 10 || month == 11) {
            //단풍 시즌
            recommendType = "FOLIAGE";
            comment = "단풍 시즌이라 야외 단풍코스를 중심으로 추천드릴게요.🍁";
        }
        else if (month == 7 || month == 8) {
            //장마철
            recommendType = "INDOOR";
            comment = "장마철이라 비가 자주 올 수 있어 실내 코스를 중심으로 추천드릴게요.☔️";
        }
        else if (month == 12 || month == 1 || month == 2) {
            //한겨울
            recommendType = "INDOOR";
            comment = "한겨울이라 추운 날씨를 고려해서 실내/짧은 야외 위주로 추천드릴게요.";
        }
        else {
            recommendType = "OUTDOOR";
            comment = "야외 활동을 즐기기 좋은 시기예요.";
        }
        
        advice.setRecommendType(recommendType);
        advice.setMessage(comment);
        
        advice.setTemp(null);
        advice.setMain(null);
        advice.setDescription(null);
        
        return advice;
    }
    
}
