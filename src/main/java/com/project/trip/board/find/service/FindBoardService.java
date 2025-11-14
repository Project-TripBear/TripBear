// 파일 경로: com.project.trip.board.find.service.FindBoardService.java (신규 생성)

package com.project.trip.board.find.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;

public interface FindBoardService {

    // 1. 목록 및 페이징
	Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword);    
    // 2. 등록/수정/삭제
    void addPost(findboardDTO dto); // 파일 업로드 처리 포함
    void updatePost(findboardDTO dto); // 파일 업로드 처리 포함
    void deletePost(int boardSeq); // 트랜잭션 필요: 게시글, 댓글, 좋아요, 스크랩 모두 삭제
    
    // 3. 상세 조회
    findboardDTO getPostDetail(int boardSeq, Integer userId); // 조회수 증가 및 좋아요/스크랩 상태 확인 포함
    findboardDTO getPostById(int boardSeq); // 순수하게 게시물 정보만 가져옴 (수정 페이지용)
    
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