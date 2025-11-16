package com.project.trip.board.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.review.mapper.ReviewCommentMapper;
import com.project.trip.board.review.model.ReviewCommentDTO;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

    @Autowired
    private ReviewCommentMapper commentMapper;

    @Override
    public List<ReviewCommentDTO> list(int reviewPostId) {
        return commentMapper.list(reviewPostId);
    }

    @Override
    public int add(ReviewCommentDTO dto) {
        return commentMapper.add(dto);
    }

    @Override
    public int del(int reviewCommentId) {
        return commentMapper.del(reviewCommentId);
    }

    @Override
    public int count(int reviewPostId) {
        return commentMapper.count(reviewPostId);
    }
    
    @Override
    public int edit(ReviewCommentDTO dto) {
        return commentMapper.edit(dto);
    }
}