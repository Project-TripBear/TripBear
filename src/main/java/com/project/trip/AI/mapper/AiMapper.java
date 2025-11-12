package com.project.trip.AI.mapper;

import java.util.List;

import com.project.trip.AI.model.HealthCareLogDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

public interface AiMapper {

	void insertAiRoute(RouteDTO route);

	void insertAiRouteStop(RouteStopDTO stop);

	RouteDTO findRouteById(long aiRouteId);

	List<RouteStopDTO> findStopsByRouteId(long aiRouteId);

	void insertHealtCareLog(HealthCareLogDTO logDTO);


}
