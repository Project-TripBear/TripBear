
package com.project.trip.board.routepost.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;
import com.project.trip.board.routepost.mapper.RoutePostMapper;

@Service
public class RoutePostServiceImpl implements RoutePostService {

    @Autowired
    private RoutePostMapper mapper;

    // ===== 게시글 =====
    @Override
    public List<RoutePostDTO> list() {
        return mapper.list();
    }

    @Override
    public RoutePostDTO get(String routepostId) {
        return mapper.get(routepostId);
    }

    @Override
    public int add(RoutePostDTO dto) {
        return mapper.add(dto);
    }

    @Override
    public int edit(RoutePostDTO dto) {
        return mapper.edit(dto);
    }

    @Override
    public int del(String routepostId) {
        // 게시글 삭제 전 이미지 삭제 (연관 데이터 정리)
        mapper.delImages(routepostId);
        return mapper.del(routepostId);
    }

    // ===== 이미지 =====
    @Override
    public List<RoutePostImageDTO> getImages(String routepostId) {
        return mapper.getImages(routepostId);
    }

    @Override
    public int addImage(RoutePostImageDTO imgDto) {
        return mapper.addImage(imgDto);
    }

    @Override
    public int delImages(String routepostId) {
        return mapper.delImages(routepostId);
    }

    // ===== 조회수 =====
    @Override
    public void increaseViewCount(String routepostId) {
        mapper.increaseViewCount(routepostId);
    }

    // ===== 좋아요 =====
    @Override
    public boolean isLiked(Map<String, Object> map) {
        return mapper.isLiked(map) > 0;
    }

    @Override
    public int addLike(Map<String, Object> map) {
        return mapper.addLike(map);
    }

    @Override
    public int removeLike(Map<String, Object> map) {
        return mapper.removeLike(map);
    }

    // ===== 스크랩 =====
    @Override
    public boolean isScrapped(Map<String, Object> map) {
        return mapper.isScrapped(map) > 0;
    }

    @Override
    public int addScrap(Map<String, Object> map) {
        return mapper.addScrap(map);
    }

    @Override
    public int removeScrap(Map<String, Object> map) {
        return mapper.removeScrap(map);
    }
}
