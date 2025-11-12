package com.project.trip.AI.service;

import com.project.trip.AI.model.UserRouteDTO;

public interface UserRouteService {
    UserRouteDTO getUserRouteWithStops(Long userRouteId);
    int deleteUserRouteCascade(Long userRouteId);
}
