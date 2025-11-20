package com.project.trip.board.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.review.mapper.ReviewCommentMapper;
import com.project.trip.board.review.model.ReviewCommentDTO;

/**
 * {@link ReviewCommentService} 인터페이스의 구현체로, 리뷰 게시글 댓글 관련 비즈니스 로직을 처리합니다.
 */
@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

    @Autowired
    private ReviewCommentMapper commentMapper;

    /**
     * 특정 리뷰 게시글에 대한 댓글 목록을 조회합니다.
     *
     * @param reviewPostId 댓글 목록을 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 댓글 목록 {@code List<ReviewCommentDTO>}
     */
    @Override
    public List<ReviewCommentDTO> list(int reviewPostId) {
        return commentMapper.list(reviewPostId);
    }

    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int add(ReviewCommentDTO dto) {
        return commentMapper.add(dto);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param reviewCommentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    public int del(int reviewCommentId) {
        return commentMapper.del(reviewCommentId);
    }

    /**
     * 특정 리뷰 게시글에 대한 댓글의 총 개수를 조회합니다.
     *
     * @param reviewPostId 댓글 개수를 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 댓글 총 개수
     */
    @Override
    public int count(int reviewPostId) {
        return commentMapper.count(reviewPostId);
    }
    
    /**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    @Override
    public int edit(ReviewCommentDTO dto) {
        return commentMapper.edit(dto);
    }
}