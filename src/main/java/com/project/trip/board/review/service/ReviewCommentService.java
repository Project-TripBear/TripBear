package com.project.trip.board.review.service;

import java.util.List;
import com.project.trip.board.review.model.ReviewCommentDTO;

public interface ReviewCommentService {

    // 댓글 목록 조회
    List<ReviewCommentDTO> list(int reviewPostId);

    // 댓글 등록
    int add(ReviewCommentDTO dto);

    // 댓글 삭제
    int del(int reviewCommentId);

    // 댓글 개수 (게시글별)
    int count(int reviewPostId);
    
    //댓글 수정
    int edit(ReviewCommentDTO dto);
}