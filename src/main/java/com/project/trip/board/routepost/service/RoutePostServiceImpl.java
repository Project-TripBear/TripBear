package com.project.trip.board.routepost.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.board.routepost.mapper.RoutePostMapper;
import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

/**
 * {@link RoutePostService} 인터페이스의 구현체로, RoutePost 게시글 관련 비즈니스 로직을 처리합니다.
 * <p>
 * 게시글 목록 조회, 상세 조회, 등록, 수정, 삭제, 이미지 관리, 조회수 증가,
 * 좋아요 및 스크랩 기능 등 게시판 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
@Service
public class RoutePostServiceImpl implements RoutePostService {

    @Autowired
    private RoutePostMapper mapper;
    
    @Autowired
    private RoutePostCommentService commentMapper;

    // ===== 게시글 =====
    /**
     * RoutePost 게시글 목록을 조회합니다.
     *
     * @param map 페이징 정보를 담은 {@code Map<String, Object>}
     * @return 조회된 RoutePost 게시글 목록 {@code List<RoutePostDTO>}
     */
    @Override
    public List<RoutePostDTO> list(Map<String, Object> map) {
        return mapper.list(map); //
    }

    /**
     * 전체 RoutePost 게시글의 수를 조회합니다.
     *
     * @param map 검색 조건을 담은 {@code Map<String, Object>}
     * @return 전체 RoutePost 게시글의 수
     */
    @Override
    public int totalCount(Map<String, Object> map) {
        return mapper.totalCount(map);
    }

    /**
     * 특정 RoutePost 게시글의 상세 정보를 조회합니다.
     *
     * @param routepostId 조회할 RoutePost 게시글의 고유 번호
     * @return 조회된 RoutePost 게시글의 상세 정보 {@link RoutePostDTO}
     */
    @Override
    public RoutePostDTO get(int routepostId) {
        return mapper.get(routepostId);
    }

    /**
     * 새로운 RoutePost 게시글을 등록합니다.
     *
     * @param dto 등록할 RoutePost 게시글 정보를 담은 {@link RoutePostDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int add(RoutePostDTO dto) {
        return mapper.add(dto);
    }

    /**
     * 기존 RoutePost 게시글을 수정합니다.
     *
     * @param dto 수정할 RoutePost 게시글 정보를 담은 {@link RoutePostDTO} 객체
     * @return 업데이트된 행의 수
     */
    @Override
    public int edit(RoutePostDTO dto) {
        return mapper.edit(dto);
    }

    /**
     * 특정 RoutePost 게시글을 삭제합니다.
     * 게시글과 관련된 댓글, 좋아요, 스크랩, 이미지 등 모든 데이터를 트랜잭션으로 함께 삭제합니다.
     *
     * @param routepostId 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    @Transactional
    public int del(int routepostId) {

        // 1) 댓글 삭제
        mapper.deleteAllComments(routepostId);

        // 2) 좋아요 삭제
        mapper.deleteAllLikes(routepostId);

        // 3) 스크랩 삭제
        mapper.deleteAllScrap(routepostId);

        // 4) 이미지 삭제
        mapper.deleteAllImages(routepostId);

        // 5) 마지막으로 게시글 삭제
        return mapper.del(routepostId);
    }


    // ===== 이미지 =====
    /**
     * 특정 RoutePost 게시글에 연결된 이미지 목록을 조회합니다.
     *
     * @param routepostId 이미지 목록을 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 이미지 목록 {@code List<RoutePostImageDTO>}
     */
    @Override
    public List<RoutePostImageDTO> getImages(int routepostId) {
        return mapper.getImages(routepostId);
    }

    /**
     * RoutePost 게시글에 이미지를 추가합니다.
     *
     * @param imgDto 추가할 이미지 정보를 담은 {@link RoutePostImageDTO} 객체
     * @return 삽입된 행의 수
     */
    @Override
    public int addImage(RoutePostImageDTO imgDto) {
        return mapper.addImage(imgDto);
    }

    /**
     * 특정 RoutePost 게시글에 연결된 모든 이미지를 삭제합니다.
     *
     * @param routepostId 이미지를 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    @Override
    public int delImages(int routepostId) {
        return mapper.delImages(routepostId);
    }

    // ===== 조회수 =====
    /**
     * 특정 RoutePost 게시글의 조회수를 1 증가시킵니다.
     *
     * @param routepostId 조회수를 증가시킬 RoutePost 게시글의 고유 번호
     */
    @Override
    public void increaseViewCount(int routepostId) {
        mapper.increaseViewCount(routepostId);
    }

 // ===== 좋아요 토글 =====
    /**
     * 특정 RoutePost 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
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
     * 특정 RoutePost 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
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
     * 특정 사용자가 특정 RoutePost 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요를 눌렀으면 true, 아니면 false
     */
    @Override
    public boolean checkLike(Map<String, Object> map) {
        return mapper.checkLike(map) > 0;
    }

    /**
     * 특정 사용자가 특정 RoutePost 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩했으면 true, 아니면 false
     */
    @Override
    public boolean checkScrap(Map<String, Object> map) {
        return mapper.checkScrap(map) > 0;
    }
    
    /**
     * 특정 이미지 ID에 해당하는 이미지를 삭제합니다.
     *
     * @param imageId 삭제할 이미지의 고유 번호
     */
    @Override
    public void deleteImageById(int imageId) {
        mapper.deleteImageById(imageId);
    }



}
