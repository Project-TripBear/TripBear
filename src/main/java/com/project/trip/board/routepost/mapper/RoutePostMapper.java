package com.project.trip.board.routepost.mapper;

import java.util.List;
import java.util.Map;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;

/**
 * 여행 경로 게시판(RoutePost)과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 게시글 목록 조회, 상세 조회, 등록, 수정, 삭제, 이미지 관리, 조회수 증가,
 * 좋아요 및 스크랩 기능 등 게시판 운영에 필요한 데이터베이스 작업을 정의합니다.
 * </p>
 */
public interface RoutePostMapper {
	
	// 게시글 목록
    /**
     * RoutePost 게시글 목록을 조회합니다.
     * 페이징 처리를 위한 시작/종료 인덱스를 포함하는 맵을 매개변수로 받습니다.
     *
     * @param map 페이징 정보를 담은 {@code Map<String, Object>} (start, end)
     * @return 조회된 RoutePost 게시글 목록 {@code List<RoutePostDTO>}
     */
    List<RoutePostDTO> list(Map<String, Object> map);

    /**
     * 전체 RoutePost 게시글의 수를 조회합니다.
     *
     * @param map 검색 조건을 담은 {@code Map<String, Object>}
     * @return 전체 RoutePost 게시글의 수
     */
    int totalCount(Map<String, Object> map);

    // 게시글 상세보기
    /**
     * 특정 RoutePost 게시글의 상세 정보를 조회합니다.
     *
     * @param routepostId 조회할 RoutePost 게시글의 고유 번호
     * @return 조회된 RoutePost 게시글의 상세 정보 {@link RoutePostDTO}
     */
    RoutePostDTO get(int routepostId);

    // 게시글 등록
    /**
     * 새로운 RoutePost 게시글을 데이터베이스에 등록합니다.
     *
     * @param dto 등록할 RoutePost 게시글 정보를 담은 {@link RoutePostDTO} 객체
     * @return 삽입된 행의 수
     */
    int add(RoutePostDTO dto);

    // 게시글 수정
    /**
     * 기존 RoutePost 게시글을 수정합니다.
     *
     * @param dto 수정할 RoutePost 게시글 정보를 담은 {@link RoutePostDTO} 객체
     * @return 업데이트된 행의 수
     */
    int edit(RoutePostDTO dto);

    // 게시글 삭제
    /**
     * 특정 RoutePost 게시글을 삭제합니다.
     *
     * @param routepostId 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int del(int routepostId);

    // 게시글 이미지 목록
    /**
     * 특정 RoutePost 게시글에 연결된 이미지 목록을 조회합니다.
     *
     * @param routepostId 이미지 목록을 조회할 RoutePost 게시글의 고유 번호
     * @return 해당 RoutePost 게시글의 이미지 목록 {@code List<RoutePostImageDTO>}
     */
    List<RoutePostImageDTO> getImages(int routepostId);

    // 이미지 등록
    /**
     * RoutePost 게시글에 이미지를 추가합니다.
     *
     * @param imgDto 추가할 이미지 정보를 담은 {@link RoutePostImageDTO} 객체
     * @return 삽입된 행의 수
     */
    int addImage(RoutePostImageDTO imgDto);

    // 이미지 삭제
    /**
     * 특정 RoutePost 게시글에 연결된 모든 이미지를 삭제합니다.
     *
     * @param routepostId 이미지를 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int delImages(int routepostId);
    
    // 조회수 증가
    /**
     * 특정 RoutePost 게시글의 조회수를 1 증가시킵니다.
     *
     * @param routepostId 조회수를 증가시킬 RoutePost 게시글의 고유 번호
     */
    void increaseViewCount(int routepostId);
    
    // ===== 좋아요 =====
    /**
     * 특정 사용자가 특정 RoutePost 게시글에 좋아요를 눌렀는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 좋아요를 눌렀으면 1 이상, 아니면 0
     */
    int checkLike(Map<String, Object> map);
    /**
     * 특정 RoutePost 게시글에 대한 사용자의 좋아요를 추가합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 삽입된 행의 수
     */
    int addLike(Map<String, Object> map);
    /**
     * 특정 RoutePost 게시글에 대한 사용자의 좋아요를 삭제합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 삭제된 행의 수
     */
    int removeLike(Map<String, Object> map);

    // ===== 스크랩 =====
    /**
     * 특정 사용자가 특정 RoutePost 게시글을 스크랩했는지 여부를 확인합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 스크랩했으면 1 이상, 아니면 0
     */
    int checkScrap(Map<String, Object> map);
    /**
     * 특정 RoutePost 게시글에 대한 사용자의 스크랩을 추가합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 삽입된 행의 수
     */
    int addScrap(Map<String, Object> map);
    /**
     * 특정 RoutePost 게시글에 대한 사용자의 스크랩을 삭제합니다.
     *
     * @param map 게시글 ID와 사용자 ID를 담은 {@code Map<String, Object>}
     * @return 삭제된 행의 수
     */
    int removeScrap(Map<String, Object> map);

    // 전체 삭제
    /**
     * 특정 RoutePost 게시글에 연결된 모든 댓글을 삭제합니다.
     *
     * @param routepostId 댓글을 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteAllComments(int routepostId);
    /**
     * 특정 RoutePost 게시글에 연결된 모든 좋아요 정보를 삭제합니다.
     *
     * @param routepostId 좋아요 정보를 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteAllLikes(int routepostId);
    /**
     * 특정 RoutePost 게시글에 연결된 모든 스크랩 정보를 삭제합니다.
     *
     * @param routepostId 스크랩 정보를 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteAllScrap(int routepostId);
    /**
     * 특정 RoutePost 게시글에 연결된 모든 이미지를 삭제합니다.
     *
     * @param routepostId 이미지를 삭제할 RoutePost 게시글의 고유 번호
     * @return 삭제된 행의 수
     */
    int deleteAllImages(int routepostId);

    /**
     * 특정 이미지 ID에 해당하는 이미지를 삭제합니다.
     *
     * @param imageId 삭제할 이미지의 고유 번호
     */
	void deleteImageById(int imageId);



}
