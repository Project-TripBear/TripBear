package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

public interface AiService {

	RouteDTO createAndSaveAiRoute(AiRouteRequestDTO preferences, long longUserId);

	RouteDTO getAiRouteById(long routeId);
}
