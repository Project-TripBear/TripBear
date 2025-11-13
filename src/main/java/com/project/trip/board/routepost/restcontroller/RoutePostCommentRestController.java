package com.project.trip.board.routepost.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.routepost.model.RoutePostCommentDTO;
import com.project.trip.board.routepost.service.RoutePostCommentService;

@RestController
@RequestMapping("/api/routepost/comment")
public class RoutePostCommentRestController {

    @Autowired
    private RoutePostCommentService commentService;

    // 댓글 목록
    @GetMapping("/list/{routepostId}")
    public List<RoutePostCommentDTO> list(@PathVariable int routepostId) {
        return commentService.list(routepostId);
    }

    // 댓글 등록
    @PostMapping("/add")
    public int add(@RequestBody RoutePostCommentDTO dto) {
        return commentService.add(dto);
    }
    
    // 댓글 수정
    @PutMapping("/edit")
    public int edit(@RequestBody RoutePostCommentDTO dto) {
        return commentService.edit(dto);
    }

    // 댓글 삭제
    @DeleteMapping("/del/{commentId}")
    public int del(@PathVariable("commentId") int commentId) {
        return commentService.del(commentId);
    }

    // 댓글 개수
    @GetMapping("/count/{routepostId}")
    public int count(@PathVariable int routepostId) {
        return commentService.count(routepostId);
    }
}
