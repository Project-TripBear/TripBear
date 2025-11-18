package com.project.trip.board.routepost.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.routepost.model.RoutePostCommentDTO;
import com.project.trip.board.routepost.service.RoutePostCommentService;

/**
 * RoutePost 게시글 댓글 관련 REST API를 처리하는 컨트롤러입니다.
 * 댓글 목록 조회, 등록, 수정, 삭제, 개수 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/routepost/comment")
public class RoutePostCommentRestController {

    @Autowired
    private RoutePostCommentService commentService;

    // 댓글 목록
    /**
     * 특정 RoutePost 게시글에 대한 댓글 목록을 조회합니다.
     *
     * @param routepostId 댓글 목록을 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 목록 {@code List<RoutePostCommentDTO>}
     */
    @GetMapping("/list/{routepostId}")
    public List<RoutePostCommentDTO> list(@PathVariable int routepostId) {
        return commentService.list(routepostId);
    }

    // 댓글 등록
    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    @PostMapping("/add")
    public int add(@RequestBody RoutePostCommentDTO dto) {
        return commentService.add(dto);
    }
    
    // 댓글 수정
    /**
     * 기존 댓글을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link RoutePostCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    @PutMapping("/edit")
    public int edit(@RequestBody RoutePostCommentDTO dto) {
        return commentService.edit(dto);
    }

    // 댓글 삭제
    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    @DeleteMapping("/del/{commentId}")
    public int del(@PathVariable("commentId") int commentId) {
        return commentService.del(commentId);
    }

    // 댓글 개수
    /**
     * 특정 RoutePost 게시글에 대한 댓글의 총 개수를 조회합니다.
     *
     * @param routepostId 댓글 개수를 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 댓글 총 개수
     */
    @GetMapping("/count/{routepostId}")
    public int count(@PathVariable int routepostId) {
        return commentService.count(routepostId);
    }
}
