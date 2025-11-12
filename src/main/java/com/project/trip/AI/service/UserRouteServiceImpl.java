package com.project.trip.AI.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.UserRouteMapper;
import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.model.UserRouteStopDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRouteServiceImpl implements UserRouteService {

    private final UserRouteMapper mapper;

    @Override
    public UserRouteDTO getUserRouteWithStops(Long userRouteId) {
        UserRouteDTO route = mapper.selectUserRouteById(userRouteId);
        if (route == null) return null;

        List<UserRouteStopDTO> stops = mapper.selectStopsByUserRouteId(userRouteId);
        route.setStops(stops);
        return route;
    }

    @Override
    @Transactional
    public int deleteUserRouteCascade(Long userRouteId) {
        mapper.deleteStopsByUserRouteId(userRouteId);
        return mapper.deleteUserRouteById(userRouteId);
    }
}
