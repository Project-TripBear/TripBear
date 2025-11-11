package com.project.trip.board.routepost.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.routepost.mapper.RoutePostCommentMapper;
import com.project.trip.board.routepost.model.RoutePostCommentDTO;

@Service("routePostCommentService")
public class RoutePostCommentServiceImpl implements RoutePostCommentService {

    @Autowired
    private RoutePostCommentMapper commentMapper;

    @Override
    public List<RoutePostCommentDTO> list(String routepostId) {
        return commentMapper.list(routepostId);
    }

    @Override
    public int add(RoutePostCommentDTO dto) {
        return commentMapper.add(dto);
    }

    @Override
    public int del(String routepostCommentId) {
        return commentMapper.del(routepostCommentId);
    }

    @Override
    public int count(String routepostId) {
        return commentMapper.count(routepostId);
    }
}
