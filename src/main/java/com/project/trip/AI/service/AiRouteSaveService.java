package com.project.trip.AI.service;

public interface AiRouteSaveService {

    // AI 추천 루트를 사용자 저장 테이블로 복사
    Long saveUserRouteFromAi(Long aiRouteId, Long userId) throws Exception;
}
