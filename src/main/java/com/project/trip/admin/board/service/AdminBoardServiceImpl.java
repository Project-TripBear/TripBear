package com.project.trip.admin.board.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map; // Map 클래스를 import 해야 합니다.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.admin.board.mapper.AdminBoardMapper;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

@Service
class AdminBoardServiceImpl implements AdminBoardService {

    @Autowired
    private AdminBoardMapper mapper;

    // 🚨 1. 전체 게시글 수를 반환하는 메서드 구현 (새로 추가)
    @Override
    public int getTotalBoardCount() {
        return mapper.getTotalBoardCount(); 
    }
    
    // 🚨 2. startRow와 endRow를 받아 목록을 조회하는 메서드 구현 (기존 메서드 대체)
    @Override
    public List<IntegratedBoardDTO> getIntegratedBoardList(int startRow, int endRow) {
        
        // MyBatis Mapper로 전달할 두 파라미터를 Map으로 구성합니다.
        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        
        return mapper.getIntegratedBoardList(paramMap); 
    }
}