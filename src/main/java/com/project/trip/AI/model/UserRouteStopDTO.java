package com.project.trip.AI.model;

import lombok.Data;


@Data
public class UserRouteStopDTO {

    private Long userRouteStopId;       // PK
    private Long userRouteId;           // FK (tblUserRoute)
    private Integer userRouteDay;       // Day
    private Integer userRouteStopOrder; // 순서
    private Double userRouteLat;        // 위도
    private Double userRouteLong;       // 경도
    private String userRouteDescription;// 장소 이름 or 설명
    private String activityCode;        // 활동 코드
    private Integer durationCategory;   // 체류시간 카테고리
    private String restaurantCategory;  // 식사 카테고리
    private String transportationMode;  // 이동수단 (CAR/WALK/BICYCLE)

    // 추가 정보 (헬스케어나 통계에서 활용)
    private Double walkingDistanceKm;
    private Integer walkingStepsCount;
    private Integer healthcareCaloriesBurned;
}
