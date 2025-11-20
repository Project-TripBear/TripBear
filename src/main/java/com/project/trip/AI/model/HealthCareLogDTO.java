package com.project.trip.AI.model;

import lombok.Data;

/**
 * AI 추천 경로를 이용한 사용자의 건강 관리 활동 기록을 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 건강 관리 ID, 사용자 ID, AI 경로 정류장 ID, 활동 날짜, 이동 거리, 걸음 수, 소모 칼로리 등
 * 건강 관리 관련 정보를 포함합니다.
 * </p>
 */
@Data
public class HealthCareLogDTO {

	/**
	 * 건강 관리 활동 기록의 고유 식별자
	 */
	private long healthcareId;
	/**
	 * 활동을 기록한 사용자의 고유 식별자
	 */
	private long userId;
	/**
	 * AI 추천 경로의 특정 정류장(경유지) 고유 식별자
	 */
	private long aiRouteStopId;
	/**
	 * 건강 관리 활동이 기록된 날짜
	 */
	private String healthcareDate;
	/**
	 * 활동을 통해 이동한 거리 (킬로미터)
	 */
	private Double healthcareDistanceKm;
	/**
	 * 활동을 통해 걸은 걸음 수
	 */
	private Integer healthcareStepsCount;
	/**
	 * 활동을 통해 소모된 칼로리
	 */
	private Double healthcareCaloriesBurned;
}
