package com.project.trip.board.review.mapper;

import java.util.List;
import com.project.trip.board.review.model.ReviewCommentDTO;

/**
 * 여행 후기 게시판의 댓글과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 댓글 목록 조회, 등록, 삭제(상태 변경), 개수 조회 및 수정 등
 * 댓글 관리에 필요한 데이터베이스 작업을 정의합니다.
 * </p>
 */
public interface ReviewCommentMapper {
    
    // 댓글 목록 조회
    /**
     * 특정 리뷰 게시글에 대한 댓글 목록을 조회합니다.
     *
     * @param reviewPostId 댓글 목록을 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 댓글 목록 {@code List<ReviewCommentDTO>}
     */
    List<ReviewCommentDTO> list(int reviewPostId);

    // 댓글 등록
    /**
     * 새로운 댓글을 데이터베이스에 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    int add(ReviewCommentDTO dto);

    // 댓글 삭제 (상태 변경)
    /**
     * 특정 댓글을 삭제(상태 변경)합니다.
     *
     * @param reviewCommentId 삭제할 댓글의 고유 번호
     * @return 삭제된(상태 변경된) 행의 수
     */
    int del(int reviewCommentId);

    // 댓글 개수 (게시글별)
    /**
     * 특정 리뷰 게시글에 달린 댓글의 총 개수를 조회합니다.
     *
     * @param reviewPostId 댓글 개수를 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 댓글 총 개수
     */
    int count(int reviewPostId);
    
    // 댓글 수정
    /**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    int edit(ReviewCommentDTO dto);
}