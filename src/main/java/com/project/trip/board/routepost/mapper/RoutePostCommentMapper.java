package com.project.trip.board.routepost.mapper;

import java.util.List;

import com.project.trip.board.routepost.model.RoutePostCommentDTO;

/**
 * 여행 경로 게시판(RoutePost)의 댓글과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 댓글 목록 조회, 등록, 삭제, 개수 조회 및 수정 등
 * 댓글 관리에 필요한 데이터베이스 작업을 정의합니다.
 * </p>
 */
public interface RoutePostCommentMapper {
	
	 // 댓글 목록 조회
    /**
     * 특정 RoutePost 게시글에 대한 댓글 목록을 조회합니다.
     *
     * @param routepostId 댓글 목록을 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 목록 {@code List<RoutePostCommentDTO>}
     */
    List<RoutePostCommentDTO> list(int routepostId);

    // 댓글 등록
    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    int add(RoutePostCommentDTO dto);

    // 댓글 삭제
    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param routepostCommentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    int del(int routepostCommentId);

    // 댓글 개수 (게시글별)
    /**
     * 특정 RoutePost 게시글에 달린 댓글의 총 개수를 조회합니다.
     *
     * @param routepostId 댓글 개수를 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 총 개수
     */
    int count(int routepostId);
    
    // 댓글 수정
    /**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    int edit(RoutePostCommentDTO dto);

}
