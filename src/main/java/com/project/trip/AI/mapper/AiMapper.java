package com.project.trip.AI.mapper;

import java.util.List;

import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

public interface AiMapper {

	void insertAiRoute(RouteDTO generateRoute);

	void insertAiRouteStop(RouteStopDTO stop);

	RouteDTO findRouteById(long routeId);

	List<RouteStopDTO> findStopsByRouteId(long routeId);


}
