package com.project.trip.AI.service;

import com.project.trip.AI.model.UserRouteDTO;

public interface UserRouteService {
    UserRouteDTO getUserRouteWithStops(Long userRouteId);
    int deleteUserRouteCascade(Long userRouteId);
    int updateStopOrder(Long stopId, int day, int order);
    int updateTransportMode(Long stopId, String mode);
}
