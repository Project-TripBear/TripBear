package com.project.trip.board.routepost.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.routepost.mapper.RoutePostMapper;
import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

@Service
public class RoutePostServiceImpl implements RoutePostService {

    @Autowired
    private RoutePostMapper mapper;

    // ===== 게시글 =====
    @Override
    public List<RoutePostDTO> list(Map<String, Object> map) {
        return mapper.list(map); //
    }

    @Override
    public RoutePostDTO get(int routepostId) {
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
    public int del(int routepostId) {
        // 게시글 삭제 전 이미지 삭제 (연관 데이터 정리)
        mapper.delImages(routepostId);
        return mapper.del(routepostId);
    }

    // ===== 이미지 =====
    @Override
    public List<RoutePostImageDTO> getImages(int routepostId) {
        return mapper.getImages(routepostId);
    }

    @Override
    public int addImage(RoutePostImageDTO imgDto) {
        return mapper.addImage(imgDto);
    }

    @Override
    public int delImages(int routepostId) {
        return mapper.delImages(routepostId);
    }

    // ===== 조회수 =====
    @Override
    public void increaseViewCount(int routepostId) {
        mapper.increaseViewCount(routepostId);
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
