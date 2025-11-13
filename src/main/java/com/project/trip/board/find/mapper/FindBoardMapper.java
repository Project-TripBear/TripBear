// 파일 경로: com.project.trip.board.find.mapper.FindBoardMapper.java (신규 생성)

package com.project.trip.board.find.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.trip.board.find.model.findboardDTO;
import com.project.trip.board.find.model.findcommentDTO;

@Mapper // (또는 Spring 설정에 따라 인터페이스만 두고 XML과 연결)
public interface FindBoardMapper {

    // 1. 목록 및 페이징
    List<findboardDTO> getList(Map<String, Object> params);
    int getTotalCount(Map<String, Object> params);
    
    // 2. 등록/수정/삭제
    int addPost(findboardDTO dto);
    int updatePost(findboardDTO dto);
    int deletePost(int boardSeq);
    
    // 3. 상세 조회
    findboardDTO getPost(int boardSeq);
    void updateViewCount(int boardSeq); // void는 update 결과가 중요하지 않을 때 사용
    
    // 4. 댓글
    int addComment(findcommentDTO dto);
    List<findcommentDTO> getCommentList(int boardSeq);
    int getCommentAuthor(int commentId);
    int updateComment(findcommentDTO dto);
    int deleteComment(int commentId);
    
    // 5. 좋아요 (추천)
    int getLikeCount(int boardSeq);
    int checkLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    void addLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    void removeLike(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    
    // 6. 스크랩
    int getScrapCount(int boardSeq);
    int checkScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    void addScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    void removeScrap(@Param("boardSeq") int boardSeq, @Param("userId") int userId);
    
    // 7. 신고 (Report DAO 로직은 분리하는 것이 좋으나, 임시로 여기에 신고 처리를 위한 Mapper를 추가)
    // - 신고 DAO의 addReport 로직은 복잡한 트랜잭션이므로 Service에서 분리하여 처리하는 것이 좋으나,
    //   여기서는 신고 테이블에 추가하는 로직만 간단히 정의합니다.
    int addReport(Map<String, Object> params); // (reportDAO의 addReport 대체)
    void updateReportStatus(int boardSeq); // 게시글 신고 상태를 PENDING으로 변경
    
    // 8. ★★★ [추가 기능] 키워드 추출 및 저장용 (대시보드 시각화 데이터 준비) ★★★
    List<Map<String, Object>> getPopularKeywords();
}