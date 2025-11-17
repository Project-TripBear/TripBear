package com.project.trip.board.review.mapper;

import java.util.List;
import java.util.Map;

import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;

public interface ReviewMapper {

    // 게시글 목록
    List<ReviewDTO> list(Map<String, Object> map);
    
    // 전체 게시글 수 (페이징)
    int totalCount(Map<String, Object> map);

    // 게시글 상세보기
    ReviewDTO get(int reviewPostId);

    // 게시글 등록
    int add(ReviewDTO dto);

    // 게시글 수정
    int edit(ReviewDTO dto);

    // 게시글 삭제
    int del(int reviewPostId);

    // 게시글 이미지 목록
    List<ReviewImageDTO> getImages(int reviewPostId);

    // 이미지 등록
    int addImage(ReviewImageDTO imgDto);

    // 이미지 삭제 (게시글 ID 기준)
    int delImages(int reviewPostId);
    
    // 조회수 증가
    void increaseViewCount(int reviewPostId);
    
    // ===== 좋아요 =====
    int checkLike(Map<String, Object> map);
    int addLike(Map<String, Object> map);
    int removeLike(Map<String, Object> map);

    // ===== 스크랩 =====
    int checkScrap(Map<String, Object> map);
    int addScrap(Map<String, Object> map);
    int removeScrap(Map<String, Object> map);

	void deleteAllComments(int reviewPostId);

	void deleteAllScrap(int reviewPostId);

	void deleteAllImages(int reviewPostId);
}