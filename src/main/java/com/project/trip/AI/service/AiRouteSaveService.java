package com.project.trip.AI.service;

/**
 * AI가 생성한 여행 경로를 사용자가 자신의 경로로 저장하는 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AiRouteSaveService {

    /**
     * AI 추천 경로를 사용자의 저장된 경로 테이블로 복사합니다.
     * <p>
     * AI 경로의 정보와 해당 경로에 포함된 경유지 정보를 복사하여
     * 새로운 사용자 경로를 생성하고 저장합니다.
     * </p>
     * @param aiRouteId 저장할 AI 여행 경로의 고유 ID
     * @param userId 경로를 저장하는 사용자의 고유 ID
     * @return 새로 저장된 사용자 경로의 고유 ID
     * @throws Exception 경로 저장 중 발생할 수 있는 예외
     */
    Long saveUserRouteFromAi(Long aiRouteId, Long userId) throws Exception;
}
