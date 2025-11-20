package com.project.trip.board.routepost.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.board.routepost.mapper.RoutePostCommentMapper;
import com.project.trip.board.routepost.model.RoutePostCommentDTO;

/**
 * {@link RoutePostCommentService} 인터페이스의 구현체로, RoutePost 게시글 댓글 관련 비즈니스 로직을 처리합니다.
 */
@Service
public class RoutePostCommentServiceImpl implements RoutePostCommentService {

    @Autowired
    private RoutePostCommentMapper commentMapper;

    /**
     * 특정 RoutePost 게시글에 대한 댓글 목록을 조회합니다.
     *
     * @param routepostId 댓글 목록을 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 목록 {@code List<RoutePostCommentDTO>}
     */
    @Override
    public List<RoutePostCommentDTO> list(int routepostId) {
        return commentMapper.list(routepostId);
    }

    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int add(RoutePostCommentDTO dto) {
        return commentMapper.add(dto);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param routepostCommentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    public int del(int routepostCommentId) {
        return commentMapper.del(routepostCommentId);
    }

    /**
     * 특정 RoutePost 게시글에 대한 댓글의 총 개수를 조회합니다.
     *
     * @param routepostId 댓글 개수를 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 총 개수
     */
    @Override
    public int count(int routepostId) {
        return commentMapper.count(routepostId);
    }
    
    /**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    @Override
    public int edit(RoutePostCommentDTO dto) { // ✅ 추가
        return commentMapper.edit(dto);
    }
}
