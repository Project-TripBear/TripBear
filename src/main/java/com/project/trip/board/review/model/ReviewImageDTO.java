package com.project.trip.board.review.model;

import lombok.Data;

@Data
public class ReviewImageDTO {
    
    private int reviewImageId;
    private int reviewPostId;
    private String reviewImageUrl;
    private int reviewImageSequence; // ERD의 '이미지 순서'
    
}