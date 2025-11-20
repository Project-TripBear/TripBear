package com.project.trip.board.review.model;

import lombok.Data;

/**
 * 리뷰 게시판의 댓글 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblReviewComment` 테이블과 매핑됩니다.
 */
@Data
public class ReviewCommentDTO {

    // --- tblReviewComment 컬럼 ---
    /**
     * 댓글의 고유 식별자 (PK)
     */
    private long reviewCommentId;
    /**
     * 댓글이 속한 리뷰 게시글의 고유 식별자 (FK)
     */
    private int reviewPostId;
    /**
     * 댓글 작성자의 사용자 고유 번호 (FK)
     */
    private int userId;
    /**
     * 댓글 내용
     */
    private String reviewCommentContent;
    /**
     * 댓글 등록일
     */
    private String reviewCommentRegdate;
    /**
     * 댓글 신고 횟수
     */
    private int reviewCommentReportCount;
    /**
     * 댓글 상태 (예: "정상", "삭제", "블라인드")
     */
    private String reviewCommentStatus;

    // --- Join ---
    /**
     * 댓글 작성자의 닉네임 (tblUser Join)
     */
    private String nickname; // (tblUser Join)
}