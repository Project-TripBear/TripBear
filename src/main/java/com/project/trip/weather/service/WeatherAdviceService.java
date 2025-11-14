
package com.project.trip.weather.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.weather.mapper.WeatherMapper;
import com.project.trip.weather.model.WeatherAdviceDTO;
import com.project.trip.weather.model.WeatherDTO;
import com.project.trip.weather.model.WeatherDataDTO;

@Service
public class WeatherAdviceService {

    @Autowired
    private WeatherService weatherService;   // 이미 OpenWeather 연동된 구현

    @Autowired
    private WeatherMapper weatherMapper;     // 방금 만든 Mapper

    /**
     * @param city 도시 이름 (예: "Seoul")
     * @param date "yyyy-MM-dd" 형식 문자열
     */
    public WeatherAdviceDTO getAdviceAndSave(String city, String date) {

        // 1. OpenWeather -> WeatherDTO
        WeatherDTO weather = weatherService.getWeather(city, date);

        WeatherAdviceDTO advice = new WeatherAdviceDTO();

        if (weather == null) {
            advice.setRecommendType("NONE");
            advice.setMessage("날씨 정보를 가져오지 못했어요. 기본 코스로 추천할게요.");
            return advice;
        }

        // WeatherAdviceDTO 에 기본 정보 채우기
        advice.setTemp(weather.getTemp());
        advice.setMain(weather.getMain());
        advice.setDescription(weather.getDescription());

        // 2. 추천 타입 계산 로직
        String mainLower = weather.getMain() != null
                ? weather.getMain().toLowerCase() : "";
        String descLower = weather.getDescription() != null
                ? weather.getDescription().toLowerCase() : "";

        boolean rain = mainLower.contains("rain") || mainLower.contains("drizzle");
        boolean snow = mainLower.contains("snow");
        boolean heavyRain = descLower.contains("heavy") || descLower.contains("storm");
        boolean lightRain = rain && !heavyRain; 
        int month = Integer.parseInt(date.substring(5, 7));
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

        // 3. DB 저장 (tblWeatherData)
        WeatherDataDTO dto = new WeatherDataDTO();
        dto.setTravelDate(Date.valueOf(date));   // java.sql.Date
        dto.setCityName(city);
        dto.setTemp(weather.getTemp());
        dto.setWeatherMain(weather.getMain());
        dto.setWeatherDesc(weather.getDescription());
        dto.setRecommendType(recommendType);
        dto.setWeatherComment(comment);

        weatherMapper.insertWeatherData(dto);

        return advice;
    }
}
