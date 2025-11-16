package com.project.trip.board.qna.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.board.qna.mapper.QnABoardMapper;
import com.project.trip.board.qna.model.PagingDTO;
import com.project.trip.board.qna.model.QnABoardDTO;
import com.project.trip.board.qna.model.QnACommentDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnABoardServiceImpl implements QnABoardService {

    private final QnABoardMapper mapper;

    /**  
     * 📌 게시글 목록 + 검색 + 카테고리 + 페이징
     */
    @Override
    public Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword, String category) {

        int postPerPage = 10;
        int end = currentPage * postPerPage;
        int start = end - postPerPage + 1;

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("searchType", searchType);
        map.put("searchKeyword", searchKeyword);
        map.put("category", category);   // 🔥 추가

        // DB 조회
        List<QnABoardDTO> list = mapper.getList(map);
        int totalCount = mapper.getTotalCount(map);

        // 페이징
        int totalPage = (int) Math.ceil(totalCount / (double) postPerPage);
        int blockSize = 10;

        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        if (endPage > totalPage) endPage = totalPage;

        boolean prev = startPage > 1;
        boolean next = endPage < totalPage;

        // 작성일 계산
        for (QnABoardDTO dto : list) {

            Date regDate = dto.getQuestion_board_regdate();
            if (regDate != null) {

                LocalDateTime reg = Instant.ofEpochMilli(regDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                LocalDateTime now = LocalDateTime.now();
                long hours = ChronoUnit.HOURS.between(reg, now);

                dto.setRegHourDiff(hours);
                dto.setRegDateFormatted(reg.toLocalDate().toString());
            }
        }

        PagingDTO paging = new PagingDTO();
        paging.setPage(currentPage);
        paging.setTotalPage(totalPage);
        paging.setStartPage(startPage);
        paging.setEndPage(endPage);
        paging.setPrev(prev);
        paging.setNext(next);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("paging", paging);
        result.put("map", map);

        return result;
    }


    // ---------------- 아래 기존 기능 그대로 유지 ---------------- //

    @Override
    public void addPost(QnABoardDTO dto) {
        mapper.addPost(dto);
    }
    
    @Override
    public void updatePost(QnABoardDTO dto) {
        mapper.updatePost(dto);
    }

    @Override
    @Transactional
    public void deletePost(int boardSeq) {
        mapper.deletePost(boardSeq);
    }

    @Override
    public QnABoardDTO getPostDetail(int boardSeq, Integer userId) {
        mapper.updateViewCount(boardSeq);
        QnABoardDTO dto = mapper.getPost(boardSeq);

        if (dto != null && userId != null) {
            dto.setLikeCount(mapper.getLikeCount(boardSeq));
            dto.setLiked(mapper.checkLike(boardSeq, userId) > 0);
            dto.setScrapCount(mapper.getScrapCount(boardSeq));
            dto.setScrapped(mapper.checkScrap(boardSeq, userId) > 0);
        }
        return dto;
    }
    
    @Override
    public QnABoardDTO getPostById(int boardSeq) {
        return mapper.getPost(boardSeq);
    }

    @Override
    public List<QnACommentDTO> getCommentList(int boardSeq) {
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
        
        mapper.addReport(params);
        mapper.updateReportStatus(boardSeq);
        
        return 1;
    }

    @Override
    public int getCommentAuthor(int commentId) {
        return mapper.getCommentAuthor(commentId);
    }

    @Override
    public void addComment(QnACommentDTO dto) {
        mapper.addComment(dto);
    }

    @Override
    public void updateComment(QnACommentDTO dto) {
        mapper.updateComment(dto);
    }

    @Override
    public void deleteComment(int commentId) {
        mapper.deleteComment(commentId);
    }
    
    @Override
    public List<QnABoardDTO> getCategoryList() {
        return mapper.getCategoryList();
    }

}
