package com.project.trip.AI.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

public interface AiRouteViewMapper {

    // AI 경로 기본 정보 조회
    RouteDTO getAiRoute(long aiRouteId);

    // AI 경로의 스탑 리스트 조회
    List<RouteStopDTO> getStops(long aiRouteId);

    // 특정 스탑 상세 조회 (선택 기능)
    RouteStopDTO getStopDetail(long aiRouteStopId);
}
