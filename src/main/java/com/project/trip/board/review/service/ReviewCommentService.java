package com.project.trip.board.review.service;

import java.util.List;
import com.project.trip.board.review.model.ReviewCommentDTO;

/**
 * 리뷰 게시글의 댓글과 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 댓글 목록 조회, 등록, 삭제, 개수 조회 및 수정 기능을 제공합니다.
 * </p>
 */
public interface ReviewCommentService {

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
     * 새로운 댓글을 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 삽입된 행의 수
     */
    int add(ReviewCommentDTO dto);

    // 댓글 삭제
    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param reviewCommentId 삭제할 댓글의 고유 번호
     * @return 삭제된 행의 수
     */
    int del(int reviewCommentId);

    // 댓글 개수 (게시글별)
    /**
     * 특정 리뷰 게시글에 대한 댓글의 총 개수를 조회합니다.
     *
     * @param reviewPostId 댓글 개수를 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 댓글 총 개수
     */
    int count(int reviewPostId);
    
    //댓글 수정
    /**
     * 댓글 내용을 수정합니다.
     *
     * @param dto 수정할 댓글 정보를 담은 {@link ReviewCommentDTO} 객체
     * @return 업데이트된 행의 수
     */
    int edit(ReviewCommentDTO dto);
}