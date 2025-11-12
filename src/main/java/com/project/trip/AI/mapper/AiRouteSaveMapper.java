package com.project.trip.AI.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiRouteSaveMapper {

    int insertUserRouteFromAi(@Param("aiRouteId") Long aiRouteId,
                              @Param("userId") Long userId);

    Long getLatestUserRouteId(@Param("userId") Long userId);

    int insertUserRouteStopsFromAi(@Param("aiRouteId") Long aiRouteId,
                                   @Param("newUserRouteId") Long newUserRouteId);
}
