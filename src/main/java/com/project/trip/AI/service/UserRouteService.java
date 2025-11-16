package com.project.trip.AI.service;

import java.util.List;

import com.project.trip.AI.model.StopOrderDTO;
import com.project.trip.AI.model.UserRouteDTO;

public interface UserRouteService {

    UserRouteDTO getUserRouteWithStops(Long userRouteId);

    int deleteUserRouteCascade(Long userRouteId);

    int updateTransportMode(Long stopId, String mode);

    int updateStopOrder(Long stopId, int day, int order);
    void updateStopOrders(int day, List<StopOrderDTO> stops);
    
    int updateStopDay(Long stopId, int day);
    int updateOrder(Long stopId, int order);

    
}
