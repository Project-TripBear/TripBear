package com.project.trip.board.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.review.mapper.ReviewMapper;
import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper mapper;

    // ===== 게시글 =====
    @Override
    public List<ReviewDTO> list(Map<String, Object> map) {
        return mapper.list(map);
    }
    
    @Override
    public int totalCount(Map<String, Object> map) {
        return mapper.totalCount(map);
    }

    @Override
    public ReviewDTO get(int reviewPostId) {
        return mapper.get(reviewPostId);
    }

    @Override
    public int add(ReviewDTO dto) {
        return mapper.add(dto);
    }

    @Override
    public int edit(ReviewDTO dto) {
        return mapper.edit(dto);
    }

    @Override
    public int del(int reviewPostId) {
        // 게시글 삭제 전 이미지 삭제 (연관 데이터 정리)
        mapper.delImages(reviewPostId);
        // (추가) 댓글, 좋아요, 스크랩 데이터도 삭제 정책에 따라 처리 필요
        return mapper.del(reviewPostId);
    }

    // ===== 이미지 =====
    @Override
    public List<ReviewImageDTO> getImages(int reviewPostId) {
        return mapper.getImages(reviewPostId);
    }

    @Override
    public int addImage(ReviewImageDTO imgDto) {
        return mapper.addImage(imgDto);
    }

    @Override
    public int delImages(int reviewPostId) {
        return mapper.delImages(reviewPostId);
    }

    // ===== 조회수 =====
    @Override
    public void increaseViewCount(int reviewPostId) {
        mapper.increaseViewCount(reviewPostId);
    }

    // ===== 좋아요 토글 =====
    @Override
    public boolean toggleLike(Map<String, Object> map) {
        if (mapper.checkLike(map) > 0) {
            mapper.removeLike(map);
            return false;   // 좋아요 취소됨
        } else {
            mapper.addLike(map);
            return true;    // 좋아요 추가됨
        }
    }

    // ===== 스크랩 토글 =====
    @Override
    public boolean toggleScrap(Map<String, Object> map) {
        if (mapper.checkScrap(map) > 0) {
            mapper.removeScrap(map);
            return false;   // 스크랩 취소됨
        } else {
            mapper.addScrap(map);
            return true;    // 스크랩 추가됨
        }
    }
    
    @Override
    public boolean checkLike(Map<String, Object> map) {
        return mapper.checkLike(map) > 0;
    }

    @Override
    public boolean checkScrap(Map<String, Object> map) {
        return mapper.checkScrap(map) > 0;
    }
}