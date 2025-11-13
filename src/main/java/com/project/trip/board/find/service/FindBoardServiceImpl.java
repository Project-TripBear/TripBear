// 파일 경로: com.project.trip.board.find.service.FindBoardServiceImpl.java (신규 생성)

package com.project.trip.board.find.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.trip.board.find.mapper.FindBoardMapper;
import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;
import com.project.trip.admin.board.model.PagingDTO; // ★★★ [수정] PagingDTO 임포트 ★★★
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로깅을 위한 Lombok 추가

@Service
@RequiredArgsConstructor
@Slf4j // 로깅 활성화
public class FindBoardServiceImpl implements FindBoardService {

    private final FindBoardMapper mapper;

    // ★★★ [추가] 파일 업로드 경로 설정 (프로퍼티 또는 상수) ★★★
	
	 @Value("${app.uploadPath}") private String uploadPath;
	 

    // 1. 목록 조회 및 페이징 (findboardList.java 대체)
    @Override
    public Map<String, Object> getPostList(int currentPage, String searchType, String searchKeyword) {
        
        // 1. 페이징 계산
        int postPerPage = 10;
        int end = currentPage * postPerPage;
        int start = end - postPerPage + 1;
        
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("searchType", searchType);
        map.put("searchKeyword", searchKeyword);
        
        // 2. DAO(Mapper) 호출
        List<findboardDTO> list = mapper.getList(map);
        int totalCount = mapper.getTotalCount(map);

        // 3. 페이징 HTML 생성 (PagingUtil 클래스가 있다고 가정)
        // String paging = PagingUtil.generatePagingHtml(currentPage, totalCount, postPerPage, searchType, searchKeyword);
        
        // 4. 결과 Map에 담아 반환
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("currentPage", currentPage);
        // result.put("paging", paging); // 페이징 HTML은 Controller 또는 JSP에서 처리하도록 단순화
        result.put("map", map);
        
        return result;
    }
    
    // 2. 게시글 등록 (addFindboard.java 대체)
    @Override
    public void addPost(findboardDTO dto) {
        // ★★★ 파일 처리 로직 (MultipartFile을 Controller에서 DTO에 담아 넘겨야 함) ★★★
        // DTO에 파일 처리 로직이 들어갈 경우, 매개변수 변경이 필요. 여기서는 DTO에 이미 파일 경로가 설정되었다고 가정.
       
        
        mapper.addPost(dto);
    }
    
    // 2-2. 게시글 수정 (editfindBoard.java의 POST 대체)
    @Override
    public void updatePost(findboardDTO dto) {
        // ★★★ 파일 처리 로직 (DTO에 이미 파일 경로가 설정되었다고 가정) ★★★
       
        mapper.updatePost(dto);
    }

    // 2-3. 게시글 삭제 (deletefindBoard.java 대체)
    @Override
    @Transactional // 트랜잭션 적용
    public void deletePost(int boardSeq) {
        // FK 제약 조건으로 인해 관련 데이터(댓글, 좋아요, 스크랩)를 먼저 삭제하는 트랜잭션 필요
        // 1. 댓글 삭제
        // mapper.deleteCommentsByBoardId(boardSeq); 
        // 2. 좋아요 삭제
        // mapper.deleteLikesByBoardId(boardSeq);
        // 3. 스크랩 삭제
        // mapper.deleteScrapsByBoardId(boardSeq);
        
        // 4. 게시글 삭제 (실제로는 외래키 옵션 CASCADE DELETE를 사용하는 것이 더 효율적)
        mapper.deletePost(boardSeq);
    }

    // 3. 상세 조회 (viewfindBoard.java 대체)
    @Override
    public findboardDTO getPostDetail(int boardSeq, Integer userId) {
        // 1. 조회수 증가
        mapper.updateViewCount(boardSeq);
        
        // 2. 게시글 정보 가져오기
        findboardDTO dto = mapper.getPost(boardSeq);

        // 3. 좋아요/스크랩 정보 설정
        if (dto != null && userId != null) {
            dto.setLikeCount(mapper.getLikeCount(boardSeq));
            dto.setLiked(mapper.checkLike(boardSeq, userId) > 0);
            dto.setScrapCount(mapper.getScrapCount(boardSeq));
            dto.setScrapped(mapper.checkScrap(boardSeq, userId) > 0);
        }
        return dto;
    }
    
    // 3-2. 순수 게시물 정보 조회
    @Override
    public findboardDTO getPostById(int boardSeq) {
        return mapper.getPost(boardSeq);
    }

    // 4. 댓글 목록 조회
    @Override
    public List<findcommentDTO> getCommentList(int boardSeq) {
        return mapper.getCommentList(boardSeq);
    }

    // 5. 좋아요 토글 (likefindBoard.java 대체)
    @Override
    public boolean toggleLike(int boardSeq, int userId) {
        if (mapper.checkLike(boardSeq, userId) > 0) {
            mapper.removeLike(boardSeq, userId);
            return false; // 취소됨
        } else {
            mapper.addLike(boardSeq, userId);
            return true; // 추가됨
        }
    }

    // 5-2. 스크랩 토글 (scrapfindBoard.java 대체)
    @Override
    public boolean toggleScrap(int boardSeq, int userId) {
        if (mapper.checkScrap(boardSeq, userId) > 0) {
            mapper.removeScrap(boardSeq, userId);
            return false; // 취소됨
        } else {
            mapper.addScrap(boardSeq, userId);
            return true; // 추가됨
        }
    }
    
    // 6. 신고 등록 (reportfindBoard.java 대체)
    @Override
    @Transactional // 신고 내역 INSERT와 게시글 상태 UPDATE는 하나의 트랜잭션으로 처리
    public int addReport(int boardSeq, int reporterId, int reportedUserId, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("boardSeq", boardSeq);
        params.put("reporterId", reporterId);
        params.put("reportedUserId", reportedUserId);
        params.put("reason", reason);
        
        // 1. 신고 내역 등록
        mapper.addReport(params);
        
        // 2. 게시글 상태 변경
        mapper.updateReportStatus(boardSeq);
        
        return 1; // 트랜잭션 성공 시
    }


    // ... 나머지 댓글 관련 Service 메서드 구현 생략 ...
    @Override
    public int getCommentAuthor(int commentId) {
        return mapper.getCommentAuthor(commentId);
    }
    // ...
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