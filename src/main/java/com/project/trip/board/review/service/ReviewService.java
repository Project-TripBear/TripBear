package com.project.trip.board.review.service;

import java.util.List;
import java.util.Map;

import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;

/**
 * 여행 후기 게시판과 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 게시글 목록 조회, 상세 조회, 등록, 수정, 삭제, 이미지 관리, 조회수 증가,
 * 좋아요 및 스크랩 기능 등 게시판 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
public interface ReviewService {

    // ===== 게시글 =====
    /**
     * 리뷰 게시글 목록을 조회합니다.
     * 페이징 처리를 위한 시작/종료 인덱스를 포함하는 맵을 매개변수로 받습니다.
     *
     * @param map 페이징 정보를 담은 {@code Map<String, Object>} (start, end)
     * @return 조회된 리뷰 게시글 목록 {@code List<ReviewDTO>}
     */
    List<ReviewDTO> list(Map<String, Object> map);
    /**
     * 전체 리뷰 게시글의 수를 조회합니다.
     *
     * @param map 검색 조건을 담은 {@code Map<String, Object>}
     * @return 전체 리뷰 게시글의 수
     */
    int totalCount(Map<String, Object> map);
    /**
     * 특정 리뷰 게시글의 상세 정보를 조회합니다.
     *
     * @param reviewPostId 조회할 리뷰 게시글의 고유 번호
     * @return 조회된 리뷰 게시글의 상세 정보 {@link ReviewDTO}
     */
    ReviewDTO get(int reviewPostId);
    /**
     * 새로운 리뷰 게시글을 등록합니다.
     *
     * @param dto 등록할 리뷰 게시글 정보를 담은 {@link ReviewDTO} 객체
     * @return 삽입된 행의 수
     */
    int add(ReviewDTO dto);
    /**
     * 기존 리뷰 게시글을 수정합니다.
     *
     * @param dto 수정할 리뷰 게시글 정보를 담은 {@link ReviewDTO} 객체
     * @return 업데이트된 행의 수
     */
    int edit(ReviewDTO dto);
    /**
     * 특정 리뷰 게시글을 삭제합니다.
     *
     * @param reviewPostId 삭제할 리뷰 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int del(int reviewPostId);

    // ===== 이미지 =====
    /**
     * 특정 리뷰 게시글에 연결된 이미지 목록을 조회합니다.
     *
     * @param reviewPostId 이미지 목록을 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 이미지 목록 {@code List<ReviewImageDTO>}
     */
    List<ReviewImageDTO> getImages(int reviewPostId);
    /**
     * 리뷰 게시글에 이미지를 추가합니다.
     *
     * @param imgDto 추가할 이미지 정보를 담은 {@link ReviewImageDTO} 객체
     * @return 삽입된 행의 수
     */
    int addImage(ReviewImageDTO imgDto);
    /**
     * 특정 리뷰 게시글에 연결된 모든 이미지를 삭제합니다.
     *
     * @param reviewPostId 이미지를 삭제할 리뷰 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int delImages(int reviewPostId);

    // ===== 조회수 =====
    /**
     * 특정 리뷰 게시글의 조회수를 1 증가시킵니다.
     *
     * @param reviewPostId 조회수를 증가시킬 리뷰 게시글의 고유 번호
     */
    void increaseViewCount(int reviewPostId);

    // ===== 좋아요/스크랩 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleLike(Map<String, Object> map);
    /**
     * 특정 리뷰 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    boolean toggleScrap(Map<String, Object> map);
    /**
     * 특정 사용자가 특정 리뷰 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
    boolean checkLike(Map<String, Object> map);
    /**
     * 특정 사용자가 특정 리뷰 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩했으면 true, 아니면 false
     */
    boolean checkScrap(Map<String, Object> map);
}