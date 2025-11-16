// 파일 경로: com.project.trip.board.find.service.FindBoardService.java
package com.project.trip.board.find.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;

public interface FindBoardService {

    // 1. 목록 및 페이징
    // [수정됨 ✅] 1-1. 사용자용 (기존 FindBoardController와 호환)
	Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword);
    // [수정됨 ✅] 1-2. 관리자용 (isAdmin 플래그 추가)
    Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword, boolean isAdmin);
    
    // 2. 등록/수정/삭제
    void addPost(findboardDTO dto); 
    void updatePost(findboardDTO dto); 
    void deletePost(int boardSeq); 
    
    // 3. 상세 조회
    findboardDTO getPostDetail(int boardSeq, Integer userId); 
    findboardDTO getPostById(int boardSeq); 
    
    // 4. 댓글
    void addComment(findcommentDTO dto);
    List<findcommentDTO> getCommentList(int boardSeq);
    int getCommentAuthor(int commentId);
    void updateComment(findcommentDTO dto);
    void deleteComment(int commentId);
    
    // 5. 좋아요/스크랩
    boolean toggleLike(int boardSeq, int userId);
    boolean toggleScrap(int boardSeq, int userId);

    // 6. 신고 (트랜잭션)
    int addReport(int boardSeq, int reporterId, int reportedUserId, String reason);
}