package com.project.trip.board.review.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;

public interface ReviewService {

    // ===== 게시글 =====
    List<ReviewDTO> list(Map<String, Object> map);
    int totalCount(Map<String, Object> map);
    ReviewDTO get(int reviewPostId);
    int add(ReviewDTO dto);
    int edit(ReviewDTO dto);
    int del(int reviewPostId);

    // ===== 이미지 =====
    List<ReviewImageDTO> getImages(int reviewPostId);
    int addImage(ReviewImageDTO imgDto);
    int delImages(int reviewPostId);

    // ===== 조회수 =====
    void increaseViewCount(int reviewPostId);

    // ===== 좋아요/스크랩 =====
    boolean toggleLike(Map<String, Object> map);
    boolean toggleScrap(Map<String, Object> map);
    boolean checkLike(Map<String, Object> map);
    boolean checkScrap(Map<String, Object> map);
}