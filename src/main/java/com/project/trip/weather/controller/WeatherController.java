// src/main/java/com/project/trip/weather/controller/WeatherController.java
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
     * GET /프로젝트컨텍스트/weather/advice?city=Seoul&date=2025-12-24
     */
    @RequestMapping(value = "/advice", method = RequestMethod.GET)
    @ResponseBody
    public WeatherAdviceDTO advice(@RequestParam("city") String city,
                                   @RequestParam("date") String date) {
        return weatherAdviceService.getAdviceAndSave(city, date);
    }
}
