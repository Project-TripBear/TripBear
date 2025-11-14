package com.project.trip.board.routepost.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.routepost.mapper.RoutePostCommentMapper;
import com.project.trip.board.routepost.model.RoutePostCommentDTO;

@Service
public class RoutePostCommentServiceImpl implements RoutePostCommentService {

    @Autowired
    private RoutePostCommentMapper commentMapper;

    @Override
    public List<RoutePostCommentDTO> list(int routepostId) {
        return commentMapper.list(routepostId);
    }

    @Override
    public int add(RoutePostCommentDTO dto) {
        return commentMapper.add(dto);
    }

    @Override
    public int del(int routepostCommentId) {
        return commentMapper.del(routepostCommentId);
    }

    @Override
    public int count(int routepostId) {
        return commentMapper.count(routepostId);
    }
    
    @Override
    public int edit(RoutePostCommentDTO dto) { // ✅ 추가
        return commentMapper.edit(dto);
    }
}
