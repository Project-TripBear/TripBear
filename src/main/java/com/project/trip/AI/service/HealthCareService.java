package com.project.trip.AI.service;

import java.time.LocalDate;

import com.project.trip.AI.model.RouteStopDTO;

/**
 * AI 추천 경로를 이용한 사용자의 건강 관리 활동 기록과 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface HealthCareService {
	
	/**
	 * 사용자의 건강 관리 활동 로그를 저장합니다.
	 * <p>
	 * 특정 경유지에서의 활동을 기반으로 사용자의 이동 거리, 걸음 수, 소모 칼로리 등을 계산하여 기록합니다.
	 * </p>
	 * @param userId 활동을 기록하는 사용자의 고유 ID
	 * @param userWeight 사용자의 몸무게 (칼로리 계산에 사용)
	 * @param stop 활동이 발생한 {@link RouteStopDTO} 객체
	 * @param tripStartDate 여행 시작 날짜
	 */
	void saveHealthCareLog(long userId, double userWeight, RouteStopDTO stop, LocalDate tripStartDate);

}
