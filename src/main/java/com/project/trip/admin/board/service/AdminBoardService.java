package com.project.trip.admin.board.service;

import java.util.List;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

/**
 * 관리자 페이지의 통합 게시판 관리와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AdminBoardService {

    /**
     * 특정 게시판의 전체 게시글 수를 조회합니다.
     * @param boardType 조회할 게시판 유형
     * @return 해당 게시판의 전체 게시글 수
     */
    int getTotalBoardCount(String boardType);

    /**
     * 페이징 및 게시판 유형에 따라 통합 게시판 목록을 조회합니다.
     * @param startRow 조회 시작 행 번호
     * @param endRow 조회 끝 행 번호
     * @param boardType 조회할 게시판 유형
     * @return {@link IntegratedBoardDTO} 객체 리스트
     */
    List<IntegratedBoardDTO> getIntegratedBoardList(int startRow, int endRow, String boardType);

	/**
     * 특정 게시글을 복구 처리합니다.
     * @param boardSeq 복구할 게시글의 고유 ID
     */
	void restorePost(int boardSeq);
}
