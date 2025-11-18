// 파일 경로: com.project.trip.board.find.mapper.FindBoardMapper.java (신규 생성)

package com.project.trip.board.qna.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.board.qna.model.QnABoardDTO;
import com.project.trip.board.qna.model.QnACommentDTO;

/**
 * Q&A 게시판과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 게시글 목록 조회, 등록, 수정, 삭제, 상세 조회, 댓글 관리, 좋아요/스크랩 기능,
 * 그리고 게시글 신고 및 키워드 추출 등 다양한 데이터베이스 작업을 정의합니다.
 * </p>
 */
@Mapper // (또는 Spring 설정에 따라 인터페이스만 두고 XML과 연결)
public interface QnABoardMapper {

    // 1. 목록 및 페이징
    /**
     * Q&A 게시글 목록을 조회합니다.
     * 페이징, 검색 타입, 검색 키워드, 카테고리 등의 조건을 포함할 수 있습니다.
     *
     * @param params 검색 및 페이징 조건을 담은 {@code Map<String, Object>}
     * @return 조건에 맞는 게시글 목록 {@code List<QnABoardDTO>}
     */
    List<QnABoardDTO> getList(Map<String, Object> params);
    /**
     * Q&A 게시글의 총 개수를 조회합니다.
     * 검색 타입, 검색 키워드, 카테고리 등의 조건을 포함할 수 있습니다.
     *
     * @param params 검색 조건을 담은 {@code Map<String, Object>}
     * @return 조건에 맞는 게시글의 총 개수
     */
    int getTotalCount(Map<String, Object> params);
    
    // 2. 등록/수정/삭제
    /**
     * 새로운 Q&A 게시글을 데이터베이스에 추가합니다.
     *
     * @param dto 추가할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     * @return 삽입된 행의 수
     */
    int addPost(QnABoardDTO dto);
    /**
     * 기존 Q&A 게시글 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     * @return 업데이트된 행의 수
     */
    int updatePost(QnABoardDTO dto);
    /**
     * 특정 Q&A 게시글을 데이터베이스에서 삭제합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deletePost(int boardSeq);
    
    // 3. 상세 조회
    /**
     * 특정 Q&A 게시글의 상세 정보를 조회합니다.
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 상세 정보 {@link QnABoardDTO}
     */
    QnABoardDTO getPost(int boardSeq);
    /**
     * 특정 Q&A 게시글의 조회수를 증가시킵니다.
     *
     * @param boardSeq 조회수를 증가시킬 게시글의 고유 번호
     */
    void updateViewCount(int boardSeq); // void는 update 결과가 중요하지 않을 때 사용
    
    // 4. 댓글
    /**
     * 새로운 댓글을 데이터베이스에 추가합니다.
     *
     * @param dto 추가할 댓글 정보를 담은 {@link QnACommentDTO} 객체
     * @return 삽입된 행의 수
     */
    int addComment(QnACommentDTO dto);
    /**
     * 특정 Q&A 게시글에 달린 댓글 목록을 조회합니다.
     *
     * @param boardSeq 댓글을 조회할 게시글의 고유 번호
     * @return 해당 게시글의 댓글 목록 {@code List<QnACommentDTO>}
     */
    List<QnACommentDTO> getCommentList(int boardSeq);
    /**
     * 특정 댓글의 작성자 ID를 조회합니다.
     *
     * @param commentId 작성자 ID를 조회할 댓글의 고유 번호
     * @return 댓글 작성자의 고유 번호
     */
    int getCommentAuthor(int commentId);
    /**
     * 기존 댓글의 내용을 업데이트합니다.
     *
     * @param dto 업데이트할 댓글 정보를 담은 {@link QnACommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    int updateComment(QnACommentDTO dto);
    /**
     * 특정 댓글을 데이터베이스에서 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteComment(int commentId);
    
    // 5. 좋아요 (추천)
    /**
     * 특정 Q&A 게시글의 좋아요(추천) 개수를 조회합니다.
     *
     * @param boardSeq 좋아요 개수를 조회할 게시글의 고유 번호
     * @return 해당 게시글의 좋아요 개수
     */
    int getLikeCount(int boardSeq);
    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param boardSeq 확인할 게시글의 고유 번호
     * @param userId   확인할 사용자의 고유 번호
     * @return 좋아요를 눌렀으면 1 이상, 아니면 0
     */
    int checkLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    /**
     * 특정 게시글에 대한 사용자의 좋아요를 추가합니다.
     *
     * @param boardSeq 좋아요를 추가할 게시글의 고유 번호
     * @param userId   좋아요를 추가하는 사용자의 고유 번호
     */
    void addLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    /**
     * 특정 게시글에 대한 사용자의 좋아요를 삭제합니다.
     *
     * @param boardSeq 좋아요를 삭제할 게시글의 고유 번호
     * @param userId   좋아요를 삭제하는 사용자의 고유 번호
     */
    void removeLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    
    // 6. 스크랩
    /**
     * 특정 Q&A 게시글의 스크랩 개수를 조회합니다.
     *
     * @param boardSeq 스크랩 개수를 조회할 게시글의 고유 번호
     * @return 해당 게시글의 스크랩 개수
     */
    int getScrapCount(int boardSeq);
    /**
     * 특정 사용자가 특정 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param boardSeq 확인할 게시글의 고유 번호
     * @param userId   확인할 사용자의 고유 번호
     * @return 스크랩했으면 1 이상, 아니면 0
     */
    int checkScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    /**
     * 특정 게시글에 대한 사용자의 스크랩을 추가합니다.
     *
     * @param boardSeq 스크랩을 추가할 게시글의 고유 번호
     * @param userId   스크랩을 추가하는 사용자의 고유 번호
     */
    void addScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    /**
     * 특정 게시글에 대한 사용자의 스크랩을 삭제합니다.
     *
     * @param boardSeq 스크랩을 삭제할 게시글의 고유 번호
     * @param userId   스크랩을 삭제하는 사용자의 고유 번호
     */
    void removeScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    
    // 7. 신고 (Report DAO 로직은 분리하는 것이 좋으나, 임시로 여기에 신고 처리를 위한 Mapper를 추가)
    // - 신고 DAO의 addReport 로직은 복잡한 트랜잭션이므로 Service에서 분리하여 처리하는 것이 좋으나,
    //   여기서는 신고 테이블에 추가하는 로직만 간단히 정의합니다.
    /**
     * Q&A 게시글 신고 정보를 데이터베이스에 추가합니다.
     *
     * @param params 신고 정보를 담은 {@code Map<String, Object>} (boardSeq, reporterId, reportedUserId, reason 포함)
     * @return 삽입된 행의 수
     */
    int addReport(Map<String, Object> params); // (reportDAO의 addReport 대체)
    /**
     * 특정 Q&A 게시글의 신고 상태를 업데이트합니다.
     *
     * @param boardSeq 신고 상태를 업데이트할 게시글의 고유 번호
     */
    void updateReportStatus(int boardSeq); // 게시글 신고 상태를 PENDING으로 변경
    
    // 8. ★★★ [추가 기능] 키워드 추출 및 저장용 (대시보드 시각화 데이터 준비) ★★★
    /**
     * 인기 키워드 목록을 조회합니다.
     * (대시보드 시각화 데이터 준비용)
     *
     * @return 인기 키워드와 관련 정보를 담은 {@code List<Map<String, Object>>}
     */
    List<Map<String, Object>> getPopularKeywords();
    /**
     * Q&A 게시글 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록 {@code List<QnABoardDTO>}
     */
	List<QnABoardDTO> getCategoryList();
}