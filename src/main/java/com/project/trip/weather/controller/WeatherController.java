// src/main/java/com/project/trip/weather/controller/WeatherController.java
package com.project.trip.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.project.trip.weather.model.WeatherAdviceDTO;
import com.project.trip.weather.service.WeatherAdviceService;

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
