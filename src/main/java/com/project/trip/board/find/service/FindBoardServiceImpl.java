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

@Service
@RequiredArgsConstructor
@Slf4j 
public class FindBoardServiceImpl implements FindBoardService {

    private final FindBoardMapper mapper;

    private final FindBoardMapper findBoardMapper;
    private final ReportMapper reportMapper;    
    
	 @Value("${app.uploadPath}") private String uploadPath;
	 

    // [수정됨 ✅] 1-1. 사용자용 (기존 FindBoardController에서 호출)
    // 이 메서드가 호출되면, 자동으로 isAdmin=false를 붙여 메인 로직을 호출합니다.
    @Override
    public Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword) {
        // (Based on the original getPostList)
        return this.getPostList(currentPage, searchType, searchKeyword, false); // isAdmin=false로 고정
    }

    // [수정됨 ✅] 1-2. 메인 로직 (관리자용 + 내부용)
    // isAdmin 플래그를 받아서 Mapper에게 전달합니다.
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
    
    @Override
    public void addPost(findboardDTO dto) {
        // ★★★ 'N'으로 수정된 FindBoardMapper.xml의 addPost 쿼리가 호출되어야 합니다 ★★★
        mapper.addPost(dto);
    }
    
    @Override
    public void updatePost(findboardDTO dto) {
        mapper.updatePost(dto);
    }

    @Override
    @Transactional
    public void deletePost(int boardSeq) {
        mapper.deletePost(boardSeq);
    }

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
    
    @Override
    public findboardDTO getPostById(int boardSeq) {
        return mapper.getPost(boardSeq);
    }

    @Override
    public List<findcommentDTO> getCommentList(int boardSeq) {
        return mapper.getCommentList(boardSeq);
    }

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
    
    @Override
    public int getCommentAuthor(int commentId) {
        return mapper.getCommentAuthor(commentId);
    }
    @Override
    public void addComment(findcommentDTO dto) {
        mapper.addComment(dto);
    }
    @Override
    public void updateComment(findcommentDTO dto) {
        mapper.updateComment(dto);
    }
    @Override
    public void deleteComment(int commentId) {
        mapper.deleteComment(commentId);
    }
}