package com.project.trip.board.routepost.model;

import java.util.List;

import lombok.Data;

/**
 * RoutePost 게시글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblRoutePost` 테이블과 매핑됩니다.
 */
@Data
public class RoutePostDTO {
	
	/**
	 * RoutePost 게시글의 고유 식별자 (PK)
	 */
	private int routepostId;
    /**
     * RoutePost 게시글 작성자의 사용자 고유 번호 (FK)
     */
    private long userId;
    /**
     * RoutePost 게시글 제목
     */
    private String routepostTitle;
    /**
     * RoutePost 게시글 내용
     */
    private String routepostContent;
    /**
     * RoutePost 게시글 만족도
     */
    private String routepostSatisfaction;
    /**
     * RoutePost 게시글 신고 상태
     */
    private String routepostReportStatus;
    /**
     * RoutePost 게시글 조회수
     */
    private int routepostViewCount;
    /**
     * RoutePost 게시글 신고 횟수
     */
    private int routepostReportCount;
    /**
     * 좋아요 수
     */
    private int likeCount;
    /**
     * 사용자 닉네임
     */
    private String nickname;           
    /**
     * RoutePost 게시글 등록일
     */
    private String routepostRegdate;
    
    /**
     * 댓글 수
     */
    private long commentCount; // 댓글 수 추가
    
    
    /**
     * 연결된 루트 ID
     */
    private int routeId; //연결된 루트 추가
	
}
