package com.project.trip.admin.board.mapper;

import java.util.List;
import java.util.Map; // Map을 사용합니다.

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.project.trip.admin.board.model.IntegratedBoardDTO;

// MyBatis Mapper 인터페이스임을 명시합니다. (필수)
@Repository 
@Mapper	
public interface AdminBoardMapper {

    // 1. 전체 게시글 수를 조회하는 메서드 (Mapper XML의 getTotalBoardCount ID와 연결)
    int getTotalBoardCount(); 

    // 2. 페이징을 위해 시작/끝 행을 받아 목록을 조회하는 메서드
    // ServiceImpl에서 Map으로 파라미터를 구성했으므로 Map으로 받습니다.
    List<IntegratedBoardDTO> getIntegratedBoardList(Map<String, Integer> paramMap);

}