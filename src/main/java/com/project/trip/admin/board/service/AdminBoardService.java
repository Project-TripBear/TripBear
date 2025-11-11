package com.project.trip.admin.board.service;

import java.util.List;
import org.apache.ibatis.annotations.Param; // ❗ 이 임포트가 반드시 필요합니다.
import com.project.trip.admin.board.model.IntegratedBoardDTO;

public interface AdminBoardService {

    // 1. 전체 게시글 수를 조회하는 메서드 추가
    int getTotalBoardCount(); 

    // 2. 파라미터에 @Param 어노테이션을 추가하여 이름(startRow, endRow)을 명시합니다.
    List<IntegratedBoardDTO> getIntegratedBoardList(
        @Param("startRow") int startRow, 
        @Param("endRow") int endRow
    );
}