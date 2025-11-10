package com.project.trip.board.routepost.service;

import java.util.List;
import com.project.trip.board.routepost.model.RoutePostCommentDTO;

public interface RoutePostCommentService {

    // 댓글 목록 조회
    List<RoutePostCommentDTO> list(String routepostId);

    // 댓글 등록
    int add(RoutePostCommentDTO dto);

    // 댓글 삭제
    int del(String routepostCommentId);

    // 댓글 개수 (게시글별)
    int count(String routepostId);
}
