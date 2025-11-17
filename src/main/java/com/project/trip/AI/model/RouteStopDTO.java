package com.project.trip.AI.model;

import lombok.Data;

/**
 * AI가 생성한 여행 경로의 각 경유지(정류장) 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 경유지 ID, 경로 ID, 일차, 순서, 위도, 경도, 설명, 활동 코드, 소요 시간, 교통수단 등
 * 경유지의 기본 정보와 함께 헬스케어 관련 정보(도보 거리, 걸음 수, 소모 칼로리, 식당 카테고리)를 포함합니다.
 * </p>
 */
@Data
public class RouteStopDTO {
	
	/**
	 * AI 여행 경로 경유지의 고유 식별자
	 */
	private long aiRouteStopId;
	/**
	 * 이 경유지가 속한 AI 여행 경로의 고유 식별자
	 */
	private long aiRouteId;
	
	/**
	 * 여행 일차 (예: 1일차, 2일차)
	 */
	private int aiRouteDay;
	/**
	 * 해당 일차 내에서의 경유지 순서
	 */
	private int aiRouteStopOrder;
	/**
	 * 경유지의 위도
	 */
	private double aiRouteLat;
	/**
	 * 경유지의 경도
	 */
	private double aiRouteLong;
	/**
	 * 경유지에 대한 설명 또는 장소 이름
	 */
	private String aiRouteDescription;
	
	/**
	 * 경유지에서 수행할 활동의 코드 (예: "sightseeing", "food")
	 */
	private String activityCode;
	/**
	 * 해당 경유지에서 머무는 예상 시간 (분 단위)
	 */
	private int durationInMinutes;
	/**
	 * 다음 경유지로 이동할 때 사용할 교통수단 (예: "walking", "car", "public_transport")
	 */
	private String transportationMode;
	

	/**
     * 해당 경유지까지의 도보 이동 거리 (킬로미터)
     */
    private double walkingDistanceKm;
    /**
     * 해당 경유지까지의 도보 이동 시 걸음 수
     */
    private int walkingStepsCount;
    /**
     * 경유지 내 식당의 카테고리 (예: "한식", "양식")
     */
    private String restaurantCategory;


    /**
     * 헬스케어 로그의 고유 식별자
     */
    private long healthcareId;
    /**
     * 해당 경유지 활동을 통해 소모된 칼로리
     */
    private double healthcareCaloriesBurned;
}
