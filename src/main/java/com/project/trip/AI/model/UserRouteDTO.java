package com.project.trip.AI.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 사용자가 저장한 여행 경로의 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 사용자 경로 ID, 사용자 ID, 원본 AI 경로 ID, 경로 제목, 여행 일수, 지역, 시작일, 종료일, 저장일 등
 * 경로의 기본 정보와 함께 경로의 경유지 목록을 포함합니다.
 * </p>
 */
@Data
public class UserRouteDTO {

    /**
     * 사용자 여행 경로의 고유 식별자 (Primary Key)
     */
    private Long userRouteId;
    /**
     * 경로를 저장한 사용자의 고유 식별자
     */
    private Long userId;
    /**
     * 이 사용자 경로의 원본이 된 AI 여행 경로의 고유 식별자 (선택 사항)
     */
    private Long sourceAiRouteId;
    /**
     * 사용자 여행 경로의 제목
     */
    private String userRouteTitle;
    /**
     * 사용자 여행 경로의 총 일수
     */
    private Integer userRouteDays;
    /**
     * 사용자 여행 경로의 지역명
     */
    private String userRouteRegion;
    /**
     * 사용자 여행 경로의 시작일
     */
    private Date userRouteStartdate;
    /**
     * 사용자 여행 경로의 종료일
     */
    private Date userRouteEnddate;
    /**
     * 사용자 여행 경로 저장일
     */
    private Date userRouteSaved; 

    /**
     * 사용자 여행 경로의 일차별 경유지 목록
     */
    private List<UserRouteStopDTO> stops;
}
