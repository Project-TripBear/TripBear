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
     * Q&A 게시글 목록을 조회하는 메인 로직을 구현합니다.
     * <p>
     * 페이징 계산, 검색 조건 및 카테고리 필터링 처리를 포함하며,
     * 각 게시글의 작성일로부터 경과 시간을 계산하여 {@link QnABoardDTO}에 설정합니다.
     * </p>
     * @param currentPage   현재 페이지 번호
     * @param searchType    검색 타입 (예: "title", "content", "writer")
     * @param searchKeyword 검색 키워드
     * @param category      조회할 카테고리
     * @return 게시글 목록, 총 개수, 페이징 정보, 검색 조건을 담은 {@code Map<String, Object>}
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

    /**
     * 새로운 Q&A 게시글을 데이터베이스에 등록합니다.
     *
     * @param dto 등록할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     */
    @Override
    public void addPost(QnABoardDTO dto) {
        mapper.addPost(dto);
    }
    
    /**
     * 기존 Q&A 게시글 정보를 데이터베이스에서 업데이트합니다.
     *
     * @param dto 업데이트할 게시글 정보를 담은 {@link QnABoardDTO} 객체
     */
    @Override
    public void updatePost(QnABoardDTO dto) {
        mapper.updatePost(dto);
    }

    /**
     * 특정 Q&A 게시글을 데이터베이스에서 삭제합니다.
     *
     * @param boardSeq 삭제할 게시글의 고유 번호
     */
    @Override
    @Transactional
    public void deletePost(int boardSeq) {
        mapper.deletePost(boardSeq);
    }

    /**
     * 특정 Q&A 게시글의 상세 정보를 조회합니다.
     * <p>
     * 게시글의 조회수를 증가시키고, 현재 로그인한 사용자의 좋아요 및 스크랩 여부를 확인하여
     * {@link QnABoardDTO} 객체에 담아 반환합니다.
     * </p>
     * @param boardSeq 조회할 게시글의 고유 번호
     * @param userId   현재 로그인한 사용자의 ID (좋아요/스크랩 여부 확인용, 비로그인 시 null)
     * @return 조회된 게시글의 상세 정보 {@link QnABoardDTO}
     */
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
    
    /**
     * 특정 Q&A 게시글의 기본 정보를 게시글 ID로 조회합니다.
     * (주로 수정 페이지 로딩 등 순수하게 게시물 정보만 가져올 때 사용)
     *
     * @param boardSeq 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 기본 정보 {@link QnABoardDTO}
     */
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
