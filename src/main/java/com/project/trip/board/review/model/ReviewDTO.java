package com.project.trip.board.review.model;

import lombok.Data;


@Data
public class ReviewDTO {

    // --- tblReviewBoard 컬럼 ---
    private int reviewPostId;
    private long userId;
    private String reviewBoardTitle;
    private String reviewBoardContent;
    private int reviewBoardCount; // 조회수
    private int reviewBoardScrapCount; // 스크랩 수 (ERD에는 있지만, RoutePost처럼 join으로 처리도 가능)
    private String reviewBoardReportStatus;
    private int reviewBoardReportCount;
    private String reviewBoardRegdate;
    private String reviewBoardUpdate;

    // --- Join 또는 추가 데이터 ---
    private String nickname; // 사용자 닉네임 (tblUser Join)
    private int likeCount; // 좋아요 수 (tblReviewLike Join)
    private long commentCount; // 댓글 수 (tblReviewComment Join)
    
}