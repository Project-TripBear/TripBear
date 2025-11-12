package com.project.trip.AI.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.model.UserRouteStopDTO;

public interface UserRouteMapper {

    UserRouteDTO selectUserRouteById(Long userRouteId);
    List<UserRouteStopDTO> selectStopsByUserRouteId(Long userRouteId);
    int deleteStopsByUserRouteId(Long userRouteId);
    int deleteUserRouteById(Long userRouteId);

    int updateStopOrder(
            @Param("stopId") Long stopId,
            @Param("day") int day,
            @Param("order") int order
        );

        int updateTransportMode(
            @Param("stopId") Long stopId,
            @Param("mode") String mode
        );
}
