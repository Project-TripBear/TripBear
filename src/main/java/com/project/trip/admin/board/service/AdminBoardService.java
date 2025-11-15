package com.project.trip.admin.board.service;

import java.util.List;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

public interface AdminBoardService {

    // 게시판 종류별 전체 게시글 수
    int getTotalBoardCount(String boardType);

    // 게시판 목록 조회
    List<IntegratedBoardDTO> getIntegratedBoardList(int startRow, int endRow, String boardType);
}
