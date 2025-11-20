package com.project.trip.board.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.board.review.mapper.ReviewMapper;
import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;

/**
 * {@link ReviewService} 인터페이스의 구현체로, 리뷰 게시글 관련 비즈니스 로직을 처리합니다.
 * <p>
 * 게시글 목록 조회, 상세 조회, 등록, 수정, 삭제, 이미지 관리, 조회수 증가,
 * 좋아요 및 스크랩 기능 등 게시판 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper mapper;

    // ===== 게시글 =====
    /**
     * 리뷰 게시글 목록을 조회합니다.
     *
     * @param map 페이징 정보를 담은 {@code Map<String, Object>}
     * @return 조회된 리뷰 게시글 목록 {@code List<ReviewDTO>}
     */
    @Override
    public List<ReviewDTO> list(Map<String, Object> map) {
        return mapper.list(map);
    }
    
    /**
     * 전체 리뷰 게시글의 수를 조회합니다.
     *
     * @param map 검색 조건을 담은 {@code Map<String, Object>}
     * @return 전체 리뷰 게시글의 수
     */
    @Override
    public int totalCount(Map<String, Object> map) {
        return mapper.totalCount(map);
    }

    /**
     * 특정 리뷰 게시글의 상세 정보를 조회합니다.
     *
     * @param reviewPostId 조회할 리뷰 게시글의 고유 번호
     * @return 조회된 리뷰 게시글의 상세 정보 {@link ReviewDTO}
     */
    @Override
    public ReviewDTO get(int reviewPostId) {
        return mapper.get(reviewPostId);
    }

    /**
     * 새로운 리뷰 게시글을 등록합니다.
     *
     * @param dto 등록할 리뷰 게시글 정보를 담은 {@link ReviewDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int add(ReviewDTO dto) {
        return mapper.add(dto);
    }

    /**
     * 기존 리뷰 게시글을 수정합니다.
     *
     * @param dto 수정할 리뷰 게시글 정보를 담은 {@link ReviewDTO} 객체
     * @return 업데이트된 행의 수
     */
    @Override
    public int edit(ReviewDTO dto) {
        return mapper.edit(dto);
    }

    /**
     * 특정 리뷰 게시글을 삭제합니다.
     * 게시글과 관련된 댓글, 좋아요, 스크랩, 이미지 등 모든 데이터를 트랜잭션으로 함께 삭제합니다.
     *
     * @param reviewPostId 삭제할 리뷰 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    @Transactional
    public int del(int reviewPostId) {

        // 1) 댓글 전체 삭제
        mapper.deleteAllComments(reviewPostId);

        // 2) 좋아요 전체 삭제
        mapper.deleteAllComments(reviewPostId);

        // 3) 스크랩 전체 삭제
        mapper.deleteAllScrap(reviewPostId);

        // 4) 이미지 전체 삭제
        mapper.deleteAllImages(reviewPostId);

        // 5) 최종 게시글 삭제
        return mapper.del(reviewPostId);
    }

    // ===== 이미지 =====
    /**
     * 특정 리뷰 게시글에 연결된 이미지 목록을 조회합니다.
     *
     * @param reviewPostId 이미지 목록을 조회할 리뷰 게시글의 고유 번호
     * @return 해당 리뷰 게시글의 이미지 목록 {@code List<ReviewImageDTO>}
     */
    @Override
    public List<ReviewImageDTO> getImages(int reviewPostId) {
        return mapper.getImages(reviewPostId);
    }

    /**
     * 리뷰 게시글에 이미지를 추가합니다.
     *
     * @param imgDto 추가할 이미지 정보를 담은 {@link ReviewImageDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int addImage(ReviewImageDTO imgDto) {
        return mapper.addImage(imgDto);
    }

    /**
     * 특정 리뷰 게시글에 연결된 모든 이미지를 삭제합니다.
     *
     * @param reviewPostId 이미지를 삭제할 리뷰 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    public int delImages(int reviewPostId) {
        return mapper.delImages(reviewPostId);
    }

    // ===== 조회수 =====
    /**
     * 특정 리뷰 게시글의 조회수를 1 증가시킵니다.
     *
     * @param reviewPostId 조회수를 증가시킬 리뷰 게시글의 고유 번호
     */
    @Override
    public void increaseViewCount(int reviewPostId) {
        mapper.increaseViewCount(reviewPostId);
    }

    // ===== 좋아요 토글 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    @Override
    public boolean toggleLike(Map<String, Object> map) {
        if (mapper.checkLike(map) > 0) {
            mapper.removeLike(map);
            return false;   // 좋아요 취소됨
        } else {
            mapper.addLike(map);
            return true;    // 좋아요 추가됨
        }
    }

    // ===== 스크랩 토글 =====
    /**
     * 특정 리뷰 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩 상태 변경 성공 여부 (true: 성공, false: 실패)
     */
    @Override
    public boolean toggleScrap(Map<String, Object> map) {
        if (mapper.checkScrap(map) > 0) {
            mapper.removeScrap(map);
            return false;   // 스크랩 취소됨
        } else {
            mapper.addScrap(map);
            return true;    // 스크랩 추가됨
        }
    }
    
    /**
     * 특정 사용자가 특정 리뷰 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
    @Override
    public boolean checkLike(Map<String, Object> map) {
        return mapper.checkLike(map) > 0;
    }

    /**
     * 특정 사용자가 특정 리뷰 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩했으면 true, 아니면 false
     */
    @Override
    public boolean checkScrap(Map<String, Object> map) {
        return mapper.checkScrap(map) > 0;
    }
}