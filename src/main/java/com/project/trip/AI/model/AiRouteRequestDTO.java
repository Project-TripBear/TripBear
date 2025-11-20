package com.project.trip.AI.model;


import lombok.Data;

/**
 * AI 기반 여행 경로 추천 시스템에 사용자가 입력하는 다양한 요청 파라미터들을 담는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 도시, 기간, 여행 스타일, 예산, 선호 지역, 교통수단, 활동 유형, 동반자 정보, 신체 정보,
 * 건강 목표, 식단 선호도, 건강 상태 등 상세한 사용자 맞춤형 여행 계획을 위한 정보를 포함합니다.
 * </p>
 */
@Data
public class AiRouteRequestDTO {
	
	/**
	 * 여행할 도시
	 */
	private String city;
	/**
	 * 여행 기간
	 */
	private String duration;
	/**
	 * 선호하는 여행 스타일
	 */
	private String travelStyle;
	/**
	 * 활동 시간대
	 */
	private String activityTime;
	/**
	 * 여행 예산
	 */
	private String budget;
	/**
	 * 선호하는 지역
	 */
	private String preferredArea;
	/**
	 * 선호하는 교통수단
	 */
	private String transportation;
	/**
	 * 선호하는 활동 유형
	 */
	private String activityType;
	/**
	 * 동반자 정보
	 */
	private String companion;
	
	/**
	 * 사용자의 신체 정보 (성별, 키, 몸무게)
	 */
	private PhysicalInfo physicalInfo;
	/**
	 * 여행 시작일
	 */
	private String startDate;
	/**
	 * 여행 종료일
	 */
	private String endDate;
	
	/**
	 * 건강 관련 여행 목표
	 */
	private String healthGoal;
	/**
	 * 식단 선호도
	 */
	private String foodPreference;
	/**
	 * 건강 상태
	 */
	private String healthCondition;
	
	/**
	 * 사용자의 신체 정보를 담는 내부 클래스입니다.
	 */
	@Data
	public static class PhysicalInfo {
		/**
		 * 성별
		 */
		private String gender;
		/**
		 * 키
		 */
		private String height;
		/**
		 * 몸무게
		 */
		private String weight;
	}
	
}
