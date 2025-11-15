package com.project.trip.AI.service;

import com.project.trip.AI.model.RouteDTO;

public interface AiRouteViewService {

    /**
     * AI 추천 경로 1개를 조회 (기본정보 + 스탑 리스트 포함)
     * @param aiRouteId AI 경로 PK
     * @return RouteDTO (AI 루트 전체 정보)
     */
    RouteDTO getAiRoute(long aiRouteId);
}
