package com.project.trip.board.review.model;

import lombok.Data;

@Data
public class ReviewCommentDTO {

    // --- tblReviewComment 컬럼 ---
    private long reviewCommentId;
    private int reviewPostId;
    private int userId;
    private String reviewCommentContent;
    private String reviewCommentRegdate;
    private int reviewCommentReportCount;
    private String reviewCommentStatus;

    // --- Join ---
    private String nickname; // (tblUser Join)
}