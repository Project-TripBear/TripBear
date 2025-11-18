package com.project.trip.board.review.model;

import java.util.List;
import lombok.Data;

/**
 * 리뷰 게시판의 게시글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblReviewBoard` 테이블과 매핑됩니다.
 */
@Data
public class ReviewDTO {

    // --- tblReviewBoard 컬럼 ---
    /**
     * 리뷰 게시글의 고유 식별자 (PK)
     */
    private int reviewPostId;
    /**
     * 리뷰 게시글 작성자의 사용자 고유 번호 (FK)
     */
    private long userId;
    /**
     * 리뷰 게시글 제목
     */
    private String reviewBoardTitle;
    /**
     * 리뷰 게시글 내용
     */
    private String reviewBoardContent;
    /**
     * 리뷰 게시글 조회수
     */
    private int reviewBoardCount; // 조회수
    /**
     * 리뷰 게시글 스크랩 수
     */
    private int reviewBoardScrapCount; // 스크랩 수 (ERD에는 있지만, RoutePost처럼 join으로 처리도 가능)
    /**
     * 리뷰 게시글 신고 상태
     */
    private String reviewBoardReportStatus;
    /**
     * 리뷰 게시글 신고 횟수
     */
    private int reviewBoardReportCount;
    /**
     * 리뷰 게시글 등록일
     */
    private String reviewBoardRegdate;
    /**
     * 리뷰 게시글 최종 수정일
     */
    private String reviewBoardUpdate;

    // --- Join 또는 추가 데이터 ---
    /**
     * 사용자 닉네임 (tblUser Join)
     */
    private String nickname; // 사용자 닉네임 (tblUser Join)
    /**
     * 좋아요 수 (tblReviewLike Join)
     */
    private int likeCount; // 좋아요 수 (tblReviewLike Join)
    /**
     * 댓글 수 (tblReviewComment Join)
     */
    private long commentCount; // 댓글 수 (tblReviewComment Join)
    
}