package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

public interface GeminiService {

	RouteDTO generateRoute(AiRouteRequestDTO preferences);

}
