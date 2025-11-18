// 파일 경로: com.project.trip.board.find.service.FindBoardServiceImpl.java
package com.project.trip.board.find.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.board.find.mapper.FindBoardMapper;
import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.common.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

/**
 * {@link FindBoardService} 인터페이스의 구현 클래스입니다.
 * <p>
 * 동행 찾기 게시판과 관련된 비즈니스 로직을 처리합니다.
 * 게시글 목록 조회, 등록, 수정, 삭제, 상세 보기, 댓글 관리, 좋아요/스크랩 기능,
 * 그리고 게시글 신고 등 게시판 운영에 필요한 다양한 기능을 제공합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j 
public class FindBoardServiceImpl implements FindBoardService {

    private final FindBoardMapper mapper;

    private final FindBoardMapper findBoardMapper;
    private final ReportMapper reportMapper;    
    
	 @Value("${app.uploadPath}") private String uploadPath;
	 

    /**
     * 사용자용 게시글 목록을 조회하고 페이징 정보를 포함하여 반환합니다.
     * <p>
     * 이 메소드는 내부적으로 {@code isAdmin} 플래그를 {@code false}로 설정하여 메인 로직을 호출합니다.
     * </p>
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @return 게시글 목록, 페이징 정보, 검색 조건을 담은 {@code Map<String, Object>}
     */
    @Override
    public Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword) {
        // (Based on the original getPostList)
        return this.getPostList(currentPage, searchType, searchKeyword, false); // isAdmin=false로 고정
    }

    /**
     * 게시글 목록을 조회하는 메인 로직을 구현합니다.
     * <p>
     * 페이징 계산 및 검색 조건 처리를 포함하며, {@code isAdmin} 플래그에 따라
     * 관리자용 또는 사용자용 게시글 목록을 조회할 수 있습니다.
     * </p>
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @param isAdmin       관리자 여부 (true: 관리자용, false: 사용자용)
     * @return 게시글 목록, 총 개수, 현재 페이지, 검색 조건을 담은 {@code Map<String, Object>}
     */
    @Override
    public Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword, boolean isAdmin) {
        // (Based on the original getPostList)
        
        // 1. 페이징 계산
        int postPerPage = 10;
        int end = currentPage * postPerPage;
        int start = end - postPerPage + 1;
        
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("searchType", searchType);
        map.put("searchKeyword", searchKeyword);
        
        // ★★★ 여기가 핵심: isAdmin 플래그를 맵에 추가합니다 ★★★
        map.put("isAdmin", isAdmin);
        
        // 2. DAO(Mapper) 호출
        List<findboardDTO> list = mapper.getList(map);
        int totalCount = mapper.getTotalCount(map);

        // 3. 페이징 HTML (생략)
        
        // 4. 결과 Map에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("currentPage", currentPage);
        result.put("map", map);
        
        return result;
    }
    
    // --- (이하 코드는 원본과 동일) ---
    
    /**
     * 새로운 게시글을 데이터베이스에 등록합니다.
     *
     * @param dto 등록할 게시글 정보를 담은 {@link findboardDTO} 객체
     */
    @Override
    public void addPost(findboardDTO dto) {
        // ★★★ 'N'으로 수정된 FindBoardMapper.xml의 addPost 쿼리가 호출되어야 합니다 ★★★
        mapper.addPost(dto);
    }
    
    /**
     * 기존 게시글 정보를 데이터베이스에서 업데이트합니다.
     *
     * @param dto 업데이트할 게시글 정보를 담은 {@link findboardDTO} 객체
     */
    @Override
    public void updatePost(findboardDTO dto) {
        mapper.updatePost(dto);
    }

    /**
     * 특정 게시글을 데이터베이스에서 삭제합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     */
    @Override
    @Transactional
    public void deletePost(int boardSeq) {
        mapper.deletePost(boardSeq);
    }

    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * <p>
     * 게시글의 조회수를 증가시키고, 현재 로그인한 사용자의 좋아요 및 스크랩 여부를 확인하여
     * {@link findboardDTO} 객체에 담아 반환합니다.
     * </p>
     * @param boardSeq 조회할 게시글의 고유 번호
     * @param userId   현재 로그인한 사용자의 ID (좋아요/스크랩 여부 확인용, 비로그인 시 null)
     * @return 조회된 게시글의 상세 정보 {@link findboardDTO}
     */
    @Override
    public findboardDTO getPostDetail(int boardSeq, Integer userId) {
        mapper.updateViewCount(boardSeq);
        findboardDTO dto = mapper.getPost(boardSeq);

        if (dto != null && userId != null) {
            dto.setLikeCount(mapper.getLikeCount(boardSeq));
            dto.setLiked(mapper.checkLike(boardSeq, userId) > 0);
            dto.setScrapCount(mapper.getScrapCount(boardSeq));
            dto.setScrapped(mapper.checkScrap(boardSeq, userId) > 0);
        }
        return dto;
    }
    
    /**
     * 특정 게시글의 기본 정보를 게시글 ID로 조회합니다.
     * (주로 수정/삭제 권한 확인 등 간단한 정보 조회에 사용)
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 기본 정보 {@link findboardDTO}
     */
    @Override
    public findboardDTO getPostById(int boardSeq) {
        return mapper.getPost(boardSeq);
    }

    /**
     * 특정 게시글에 달린 모든 댓글 목록을 조회합니다.
     *
     * @param boardSeq 댓글을 조회할 게시글의 고유 번호
     * @return 조회된 댓글 {@link findcommentDTO}의 리스트
     */
    @Override
    public List<findcommentDTO> getCommentList(int boardSeq) {
        return mapper.getCommentList(boardSeq);
    }

    /**
     * 특정 게시글에 대한 사용자의 좋아요 상태를 토글합니다.
     * <p>
     * 사용자가 이미 좋아요를 눌렀으면 취소하고, 아니면 좋아요를 추가합니다.
     * </p>
     * @param boardSeq 좋아요를 토글할 게시글의 고유 번호
     * @param userId   좋아요를 요청한 사용자의 ID
     * @return 좋아요 상태 변경 성공 여부 (true: 좋아요 추가, false: 좋아요 취소)
     */
    @Override
    public boolean toggleLike(int boardSeq, int userId) {
        if (mapper.checkLike(boardSeq, userId) > 0) {
            mapper.removeLike(boardSeq, userId);
            return false; 
        } else {
            mapper.addLike(boardSeq, userId);
            return true; 
        }
    }

    /**
     * 특정 게시글에 대한 사용자의 스크랩 상태를 토글합니다.
     * <p>
     * 사용자가 이미 스크랩했으면 취소하고, 아니면 스크랩을 추가합니다.
     * </p>
     * @param boardSeq 스크랩을 토글할 게시글의 고유 번호
     * @param userId   스크랩을 요청한 사용자의 ID
     * @return 스크랩 상태 변경 성공 여부 (true: 스크랩 추가, false: 스크랩 취소)
     */
    @Override
    public boolean toggleScrap(int boardSeq, int userId) {
        if (mapper.checkScrap(boardSeq, userId) > 0) {
            mapper.removeScrap(boardSeq, userId);
            return false; 
        } else {
            mapper.addScrap(boardSeq, userId);
            return true; 
        }
    }
    
    /**
     * 게시글 신고를 처리합니다.
     * <p>
     * 신고 정보를 데이터베이스에 추가하고, 해당 게시글의 신고 상태를 업데이트합니다.
     * </p>
     * @param boardSeq       신고할 게시글의 고유 번호
     * @param reporterId     신고자의 ID
     * @param reportedUserId 신고 대상 사용자의 ID
     * @param reason         신고 사유
     * @return 처리된 신고 건수
     */
    @Override
    @Transactional
    public int addReport(int boardSeq, int reporterId, int reportedUserId, String reason) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("boardSeq", boardSeq);
        params.put("reporterId", reporterId);
        params.put("reportedUserId", reportedUserId);
        params.put("reason", reason);
        params.put("report_target_type", "findboard");
        
        reportMapper.addReport(params);
        findBoardMapper.updateReportStatus(boardSeq);
        
        return 1; 
    }
    
    /**
     * 특정 댓글의 작성자 ID를 조회합니다.
     *
     * @param commentId 작성자 ID를 조회할 댓글의 고유 번호
     * @return 댓글 작성자의 사용자 ID
     */
    @Override
    public int getCommentAuthor(int commentId) {
        return mapper.getCommentAuthor(commentId);
    }
    /**
     * 새로운 댓글을 데이터베이스에 등록합니다.
     *
     * @param dto 등록할 댓글 정보를 담은 {@link findcommentDTO} 객체
     */
    @Override
    public void addComment(findcommentDTO dto) {
        mapper.addComment(dto);
    }
    /**
     * 기존 댓글 정보를 데이터베이스에서 업데이트합니다.
     *
     * @param dto 업데이트할 댓글 정보를 담은 {@link findcommentDTO} 객체
     */
    @Override
    public void updateComment(findcommentDTO dto) {
        mapper.updateComment(dto);
    }
    /**
     * 특정 댓글을 데이터베이스에서 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 번호
     */
    @Override
    public void deleteComment(int commentId) {
        mapper.deleteComment(commentId);
    }
}