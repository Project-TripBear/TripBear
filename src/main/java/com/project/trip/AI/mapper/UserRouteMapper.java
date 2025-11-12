package com.project.trip.AI.mapper;

import java.util.List;

import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.model.UserRouteStopDTO;

public interface UserRouteMapper {

    UserRouteDTO selectUserRouteById(Long userRouteId);

    List<UserRouteStopDTO> selectStopsByUserRouteId(Long userRouteId);

    int deleteStopsByUserRouteId(Long userRouteId);

    int deleteUserRouteById(Long userRouteId);

}
