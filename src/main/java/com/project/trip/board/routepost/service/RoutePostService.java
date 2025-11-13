package com.project.trip.board.routepost.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

public interface RoutePostService {

    // ===== 게시글 =====
	List<RoutePostDTO> list(Map<String, Object> map);
    RoutePostDTO get(int routepostId);
    int add(RoutePostDTO dto);
    int edit(RoutePostDTO dto);
    int del(int routepostId);

    // ===== 이미지 =====
    List<RoutePostImageDTO> getImages(int routepostId);
    int addImage(RoutePostImageDTO imgDto);
    int delImages(int i);

    // ===== 조회수 =====
    void increaseViewCount(int routepostId);

    boolean toggleLike(Map<String, Object> map);
    boolean toggleScrap(Map<String, Object> map);

	
}
