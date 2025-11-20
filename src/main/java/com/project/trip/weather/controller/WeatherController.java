package com.project.trip.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.project.trip.weather.model.WeatherAdviceDTO;
import com.project.trip.weather.service.WeatherAdviceService;

/**
 * 날씨 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 특정 도시와 날짜에 대한 날씨 조언을 제공하는 기능을 담당합니다.
 * </p>
 */
@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherAdviceService weatherAdviceService;

    /**
     * 특정 도시와 날짜에 대한 날씨 조언 정보를 반환합니다.
     * <p>
     * 예시 요청: {@code GET /프로젝트컨텍스트/weather/advice?city=Seoul&date=2025-12-24}
     * </p>
     * @param city 조회할 도시 이름 (예: Seoul)
     * @param date 조회할 날짜 (예: 2025-12-24)
     * @return 날씨 조언 정보를 담은 {@link WeatherAdviceDTO} 객체
     */
    @RequestMapping(value = "/advice", method = RequestMethod.GET)
    @ResponseBody
    public WeatherAdviceDTO advice(@RequestParam("city") String city,
                                   @RequestParam("date") String date) {
        return weatherAdviceService.getAdviceAndSave(city, date);
    }
}