package com.project.trip.admin.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.admin.board.mapper.AdminBoardMapper;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

/**
 * {@link AdminBoardService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AdminBoardMapper}를 통해 데이터베이스와 연동하여 관리자 페이지의 통합 게시판 관리
 * (목록 조회, 게시글 수 조회, 복구 등) 관련 비즈니스 로직을 처리합니다.
 * </p>
 */
@Service
public class AdminBoardServiceImpl implements AdminBoardService {

    @Autowired
    private AdminBoardMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalBoardCount(String boardType) {
        return mapper.getTotalBoardCount(boardType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IntegratedBoardDTO> getIntegratedBoardList(int startRow, int endRow, String boardType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        paramMap.put("boardType", boardType);
        return mapper.getIntegratedBoardList(paramMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restorePost(int boardSeq) {
        mapper.restorePost(boardSeq);
    }

}
