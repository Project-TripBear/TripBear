package com.project.trip.admin.board.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

/**
 * 관리자 페이지의 통합 게시판 관리와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 여러 게시판의 글을 통합하여 조회하거나 관리하는 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
@Mapper
public interface AdminBoardMapper {

    /**
     * 특정 게시판의 전체 게시글 수를 조회합니다.
     * @param boardType 조회할 게시판 유형
     * @return 해당 게시판의 전체 게시글 수
     */
    int getTotalBoardCount(@Param("boardType") String boardType);

    /**
     * 페이징 및 검색 조건에 따라 통합 게시판 목록을 조회합니다.
     * @param paramMap 페이징 및 검색 조건을 담은 Map (boardType, startRow, endRow 등)
     * @return {@link IntegratedBoardDTO} 객체 리스트
     */
    List<IntegratedBoardDTO> getIntegratedBoardList(Map<String, Object> paramMap);

    /**
     * 특정 게시글을 복구 처리합니다.
     * <p>
     * 삭제되었거나 비공개 처리된 게시글을 다시 공개 상태로 변경할 때 사용될 수 있습니다.
     * </p>
     * @param boardSeq 복구할 게시글의 고유 ID
     */
    void restorePost(int boardSeq);

}
