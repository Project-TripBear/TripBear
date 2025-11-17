package com.project.trip.AI.service;

import com.project.trip.AI.model.RouteDTO;

/**
 * AI가 생성한 여행 경로를 조회하는 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AiRouteViewService {

    /**
     * 특정 AI 추천 경로의 전체 정보를 조회합니다.
     * <p>
     * AI 경로의 기본 정보와 해당 경로에 포함된 모든 경유지 목록을 함께 조회합니다.
     * </p>
     * @param aiRouteId 조회할 AI 여행 경로의 고유 ID
     * @return AI 여행 경로의 전체 정보를 담은 {@link RouteDTO} 객체
     */
    RouteDTO getAiRoute(long aiRouteId);
}
