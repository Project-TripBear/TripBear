// 파일 경로: com.project.trip.board.find.service.FindBoardService.java (신규 생성)

package com.project.trip.board.qna.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.qna.model.QnABoardDTO;
import com.project.trip.board.qna.model.QnACommentDTO;

public interface QnABoardService {

    /**
     * Q&A 게시글 목록을 조회하고 페이징, 검색, 카테고리 필터링 정보를 포함하여 반환합니다.
     *
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @param category      조회할 카테고리
     * @return 게시글 목록, 페이징 정보, 검색 조건을 담은 {@code Map<String, Object>}
     */
	Map<String, Object> getPostList(
	        int currentPage,
	        String searchType,
	        String searchKeyword,
	        String category
	);
    
    /**
     * 새로운 Q&A 게시글을 등록합니다.
     *
     * @param dto 등록할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     */
    void addPost(QnABoardDTO dto);
    /**
     * 기존 Q&A 게시글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     */
    void updatePost(QnABoardDTO dto);
    /**
     * 특정 Q&A 게시글을 삭제합니다.
     * 게시글과 관련된 댓글, 좋아요, 스크랩 등 모든 데이터를 트랜잭션으로 함께 삭제합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     */
    void deletePost(int boardSeq);
    
    /**
     * 특정 Q&A 게시글의 상세 정보를 조회합니다.
     * 조회수 증가 처리 및 현재 사용자의 좋아요/스크랩 여부를 포함하여 반환합니다.
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @param userId   현재 로그인한 사용자의 ID (좋아요/스크랩 여부 확인용, 비로그인 시 null)
     * @return 조회된 게시글의 상세 정보 {@link QnABoardDTO}
     */
    QnABoardDTO getPostDetail(int boardSeq, Integer userId);
    /**
     * 특정 Q&A 게시글의 기본 정보를 게시글 ID로 조회합니다.
     * (주로 수정 페이지 로딩 등 순수하게 게시물 정보만 가져올 때 사용)
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 기본 정보 {@link QnABoardDTO}
     */
    QnABoardDTO getPostById(int boardSeq);
    
    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link QnACommentDTO} 객체
     */
    void addComment(QnACommentDTO dto);
    /**
     * 특정 게시글에 달린 모든 댓글 목록을 조회합니다.
     *
     * @param boardSeq 댓글을 조회할 게시글의 고유 번호
     * @return 조회된 댓글 {@link QnACommentDTO}의 리스트
     */
    List<QnACommentDTO> getCommentList(int boardSeq);
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
     * @param dto 업데이트할 댓글 정보를 담은 {@link QnACommentDTO} 객체
     */
    void updateComment(QnACommentDTO dto);
    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     */
    void deleteComment(int commentId);
    
    /**
     * 특정 Q&A 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     * (좋아요 추가 또는 삭제)
     *
     * @param boardSeq 좋아요를 토글할 게시글의 고유 번호
     * @param userId   좋아요를 요청한 사용자의 ID
     * @return 좋아요 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleLike(int boardSeq, int userId);
    /**
     * 특정 Q&A 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     * (스크랩 추가 또는 삭제)
     *
     * @param boardSeq 스크랩을 토글할 게시글의 고유 번호
     * @param userId   스크랩을 요청한 사용자의 ID
     * @return 스크랩 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleScrap(int boardSeq, int userId);

    /**
     * Q&A 게시글 신고를 처리합니다.
     *
     * @param boardSeq       신고할 게시글의 고유 번호
     * @param reporterId     신고자의 ID
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param reason         신고 사유
     * @return 처리된 신고 건수
     */
    int addReport(int boardSeq, int reporterId, int reportedUserId, String reason);
    /**
     * Q&A 게시글 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록 (일반적으로 {@code List<CategoryDTO>} 형태)
     */
	Object getCategoryList();
    
 
}