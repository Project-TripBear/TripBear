package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.AI.mapper.AiRouteViewMapper;
import com.project.trip.AI.model.RouteDTO;

@Service
public class AiRouteViewServiceImpl implements AiRouteViewService {

    @Autowired
    private AiRouteViewMapper mapper;

    @Override
    public RouteDTO getAiRoute(long aiRouteId) {
        // ① 기본 경로 정보 조회
        RouteDTO route = mapper.getAiRoute(aiRouteId);

        // ② 스탑(경유지) 목록 추가
        if (route != null) {
            route.setStops(mapper.getStops(aiRouteId));
        }

        return route;
    }
}
