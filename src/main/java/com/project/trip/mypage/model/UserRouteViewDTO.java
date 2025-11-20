 package com.project.trip.mypage.model;

import lombok.Data;

/**
 * 마이페이지에서 사용자 정의 루트(여행 경로) 정보를 조회하기 위한 데이터 전송 객체(DTO)입니다.
 * 사용자가 생성한 여행 경로의 상세 정보를 포함합니다.
 */
@Data
public class UserRouteViewDTO {


    /**
     * 사용자 루트 고유 번호 (user_route_id)
     */
    private String seq;                 // user_route_id
    /**
     * 사용자 ID (user_id)
     */
    private String useq;                // user_id
    /**
     * 사용자 루트 제목 (user_route_title)
     */
    private String userroutetitle;      // user_route_title
    /**
     * 사용자 루트 여행 일수 (user_route_days)
     */
    private String userroutedays;       // user_route_days
    /**
     * 사용자 루트 시작 날짜 (user_route_startdate)
     */
    private String userroutestartdate;  // user_route_startdate
    /**
     * 사용자 루트 종료 날짜 (user_route_enddate)
     */
    private String userrouteenddate;    // user_route_enddate
    
    /**
     * 사용자 루트 지역 (예약 URL에 전달될 데이터)
     */
    private String userrouteregion;		//예약 url에 보낼 데이터
}
