package com.project.trip.board.routepost.mapper;

import java.util.List;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

public interface RoutePostMapper {
	
	// 게시글 목록
    List<RoutePostDTO> list();

    // 게시글 상세보기
    RoutePostDTO get(String routepostId);

    // 게시글 등록
    int add(RoutePostDTO dto);

    // 게시글 수정
    int edit(RoutePostDTO dto);

    // 게시글 삭제
    int del(String routepostId);

    // 게시글 이미지 목록
    List<RoutePostImageDTO> getImages(String routepostId);

    // 이미지 등록
    int addImage(RoutePostImageDTO imgDto);

    // 이미지 삭제
    int delImages(String routepostId);

}
