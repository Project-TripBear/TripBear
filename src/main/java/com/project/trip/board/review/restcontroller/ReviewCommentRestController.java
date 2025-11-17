package com.project.trip.board.review.restcontroller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.trip.board.review.model.ReviewCommentDTO;
import com.project.trip.board.review.service.ReviewCommentService;

@RestController
@RequestMapping("/api/review/comment")
public class ReviewCommentRestController {

    @Autowired
    private ReviewCommentService commentService;

    // ===== 댓글 목록 =====
    @GetMapping("/list/{reviewPostId}")
    public List<ReviewCommentDTO> list(@PathVariable int reviewPostId) {
        return commentService.list(reviewPostId);
    }

    // ===== 댓글 등록 =====
    @PostMapping("/add")
    public int add(@RequestBody ReviewCommentDTO dto) {
        return commentService.add(dto);
    }

    // ===== 댓글 수정 =====
    @PutMapping("/edit")
    public int edit(@RequestBody ReviewCommentDTO dto) {
        return commentService.edit(dto);
    }

    // ===== 댓글 삭제 =====
    @DeleteMapping("/del/{commentId}")
    public int del(@PathVariable("commentId") int commentId) {
        return commentService.del(commentId);
    }

    // ===== 댓글 개수 =====
    @GetMapping("/count/{reviewPostId}")
    public int count(@PathVariable int reviewPostId) {
        return commentService.count(reviewPostId);
    }
}
