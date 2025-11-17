package com.project.trip.AI.service;

import com.project.trip.AI.model.AiRouteRequestDTO;
import com.project.trip.AI.model.RouteDTO;

/**
 * AI 기반 여행 경로 생성 및 조회와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AiService {

	/**
	 * 특정 AI 여행 경로 ID에 해당하는 경로 정보를 조회합니다.
	 * @param routeId 조회할 AI 여행 경로의 고유 ID
	 * @return AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
	 */
	RouteDTO getAiRouteById(long routeId);

	/**
	 * 사용자 선호도를 기반으로 AI 여행 경로를 생성하고 데이터베이스에 저장합니다.
	 * @param preferences 사용자 선호도 정보가 담긴 {@link AiRouteRequestDTO} 객체
	 * @param longUserId 경로를 생성하는 사용자의 고유 ID
	 * @param userWeight 사용자의 몸무게 (헬스케어 계산에 사용될 수 있음)
	 * @return 생성 및 저장된 AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
	 */
	RouteDTO createAndSaveAiRoute(AiRouteRequestDTO preferences, long longUserId, double userWeight);
}
