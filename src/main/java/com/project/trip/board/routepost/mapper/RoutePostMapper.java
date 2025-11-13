package com.project.trip.board.routepost.mapper;

import java.util.List;
import java.util.Map;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

public interface RoutePostMapper {
	
	// 게시글 목록
    List<RoutePostDTO> list(Map<String, Object> map);

    // 게시글 상세보기
    RoutePostDTO get(int routepostId);

    // 게시글 등록
    int add(RoutePostDTO dto);

    // 게시글 수정
    int edit(RoutePostDTO dto);

    // 게시글 삭제
    int del(int routepostId);

    // 게시글 이미지 목록
    List<RoutePostImageDTO> getImages(int routepostId);

    // 이미지 등록
    int addImage(RoutePostImageDTO imgDto);

    // 이미지 삭제
    int delImages(int routepostId);
    
    // 조회수 증가
    void increaseViewCount(int routepostId);
    
    // ===== 좋아요 =====
    int checkLike(Map<String, Object> map);
    int addLike(Map<String, Object> map);
    int removeLike(Map<String, Object> map);

    // ===== 스크랩 =====
    int checkScrap(Map<String, Object> map);
    int addScrap(Map<String, Object> map);
    int removeScrap(Map<String, Object> map);


}
