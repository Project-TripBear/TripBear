package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.weather.model.WeatherDTO;


public interface GeminiService {

	RouteDTO generateRoute(AiRouteRequestDTO dto, WeatherDTO weather);

}
