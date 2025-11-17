// 파일 경로: com.project.trip.board.find.service.FindBoardService.java
package com.project.trip.board.find.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;

public interface FindBoardService {

    // 1. 목록 및 페이징
    /**
     * 사용자용 게시글 목록을 조회하고 페이징 정보를 포함하여 반환합니다.
     *
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @return 게시글 목록, 페이징 정보, 검색 조건을 담은 {@code Map<String, Object>}
     */
	Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword);
    /**
     * 관리자용 게시글 목록을 조회하고 페이징 정보를 포함하여 반환합니다.
     * 관리자 여부에 따라 추가적인 로직이 적용될 수 있습니다.
     *
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @param isAdmin       관리자 여부 (true: 관리자용, false: 사용자용)
     * @return 게시글 목록, 페이징 정보, 검색 조건을 담은 {@code Map<String, Object>}
     */
    Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword, boolean isAdmin);
    
    // 2. 등록/수정/삭제
    /**
     * 새로운 게시글을 등록합니다.
     *
     * @param dto 등록할 게시글 정보를 담은 {@link findboardDTO} 객체
     */
    void addPost(findboardDTO dto); 
    /**
     * 기존 게시글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 게시글 정보를 담은 {@link findboardDTO} 객체
     */
    void updatePost(findboardDTO dto); 
    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     */
    void deletePost(int boardSeq); 
    
    // 3. 상세 조회
    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * 조회수 증가 처리 및 현재 사용자의 좋아요/스크랩 여부를 포함하여 반환합니다.
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @param userId   현재 로그인한 사용자의 ID (좋아요/스크랩 여부 확인용, 비로그인 시 null)
     * @return 조회된 게시글의 상세 정보 {@link findboardDTO}
     */
    findboardDTO getPostDetail(int boardSeq, Integer userId); 
    /**
     * 특정 게시글의 기본 정보를 게시글 ID로 조회합니다.
     * (주로 수정/삭제 권한 확인 등 간단한 정보 조회에 사용)
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 기본 정보 {@link findboardDTO}
     */
    findboardDTO getPostById(int boardSeq); 
    
    // 4. 댓글
    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link findcommentDTO} 객체
     */
    void addComment(findcommentDTO dto);
    /**
     * 특정 게시글에 달린 모든 댓글 목록을 조회합니다.
     *
     * @param boardSeq 댓글을 조회할 게시글의 고유 번호
     * @return 조회된 댓글 {@link findcommentDTO}의 리스트
     */
    List<findcommentDTO> getCommentList(int boardSeq);
    /**
     * 특정 댓글의 작성자 ID를 조회합니다.
     *
     * @param commentId 작성자 ID를 조회할 댓글의 고유 번호
     * @return 댓글 작성자의 사용자 ID
     */
    int getCommentAuthor(int commentId);
    /**
     * 기존 댓글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 댓글 정보를 담은 {@link findcommentDTO} 객체
     */
    void updateComment(findcommentDTO dto);
    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     */
    void deleteComment(int commentId);
    
    // 5. 좋아요/스크랩
    /**
     * 특정 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     * (좋아요 추가 또는 삭제)
     *
     * @param boardSeq 좋아요를 토글할 게시글의 고유 번호
     * @param userId   좋아요를 요청한 사용자의 ID
     * @return 좋아요 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleLike(int boardSeq, int userId);
    /**
     * 특정 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     * (스크랩 추가 또는 삭제)
     *
     * @param boardSeq 스크랩을 토글할 게시글의 고유 번호
     * @param userId   스크랩을 요청한 사용자의 ID
     * @return 스크랩 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleScrap(int boardSeq, int userId);

    // 6. 신고 (트랜잭션)
    /**
     * 게시글 신고를 처리합니다.
     *
     * @param boardSeq       신고할 게시글의 고유 번호
     * @param reporterId     신고자의 ID
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param reason         신고 사유
     * @return 처리된 신고 건수
     */
    int addReport(int boardSeq, int reporterId, int reportedUserId, String reason);
}