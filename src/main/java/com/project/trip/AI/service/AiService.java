package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

public interface AiService {

	RouteDTO getAiRouteById(long routeId);

	RouteDTO createAndSaveAiRoute(AiRouteRequestDTO preferences, long longUserId, double userWeight);
}
