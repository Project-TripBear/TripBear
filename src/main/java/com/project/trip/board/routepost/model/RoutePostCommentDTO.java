package com.project.trip.board.routepost.model;

import lombok.Data;

/**
 * RoutePost 게시글의 댓글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblRoutePostComment` 테이블과 매핑됩니다.
 */
@Data
public class RoutePostCommentDTO {

	/**
	 * 댓글의 고유 식별자 (PK)
	 */
	private long routepostCommentId;
    /**
     * 댓글 작성자의 사용자 고유 번호 (FK)
     */
    private int userId;
    /**
     * 댓글이 속한 RoutePost 게시글의 고유 식별자 (FK)
     */
    private int routepostId;
    /**
     * 댓글 내용
     */
    private String routepostContent;
    /**
     * 댓글 등록일
     */
    private String routepostRegdate;
    /**
     * 댓글 신고 횟수
     */
    private int routepostCommentReportCount;
    /**
     * 댓글 상태 (예: "정상", "삭제", "블라인드")
     */
    private String commentStatus;
    /**
     * 댓글 작성자의 닉네임 (JOIN용)
     */
    private String nickname; // JOIN용
	
}
