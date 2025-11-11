package com.project.trip.AI.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserRouteDTO {

    private Long userRouteId;          // PK
    private Long userId;               // 사용자 ID
    private Long aiRouteId;            // 원본 AI 루트 ID (optional)
    private String userRouteTitle;     // 루트 제목
    private Integer userRouteDays;     // 여행 일수
    private String userRouteRegion;    // 지역명
    private Date userRouteStartdate;   // 시작일
    private Date userRouteEnddate;     // 종료일
    private Date userRouteCreated;     // 생성일

    private List<UserRouteStopDTO> stops; // 일차별 스탑 리스트
}
