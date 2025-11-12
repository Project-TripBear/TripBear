package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.WeatherDTO;

public interface GeminiService {

	RouteDTO generateRoute(AiRouteRequestDTO preferences, WeatherDTO weather);

@Service
public class GeminiService {
    public String test() {
        return "Gemini test";
    }
}
