package com.project.trip.board.routepost.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

public interface RoutePostService {

    // ===== 게시글 =====
	List<RoutePostDTO> list(Map<String, Object> map);
    RoutePostDTO get(String routepostId);
    int add(RoutePostDTO dto);
    int edit(RoutePostDTO dto);
    int del(String routepostId);

    // ===== 이미지 =====
    List<RoutePostImageDTO> getImages(String routepostId);
    int addImage(RoutePostImageDTO imgDto);
    int delImages(int i);

    // ===== 조회수 =====
    void increaseViewCount(String routepostId);

    // ===== 좋아요 =====
    boolean isLiked(Map<String, Object> map);
    int addLike(Map<String, Object> map);
    int removeLike(Map<String, Object> map);

    // ===== 스크랩 =====
    boolean isScrapped(Map<String, Object> map);
    int addScrap(Map<String, Object> map);
    int removeScrap(Map<String, Object> map);
}
