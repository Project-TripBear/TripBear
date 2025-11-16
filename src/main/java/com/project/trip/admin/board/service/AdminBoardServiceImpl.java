package com.project.trip.admin.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.admin.board.mapper.AdminBoardMapper;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

@Service
public class AdminBoardServiceImpl implements AdminBoardService {

    @Autowired
    private AdminBoardMapper mapper;

    @Override
    public int getTotalBoardCount(String boardType) {
        return mapper.getTotalBoardCount(boardType);
    }

    @Override
    public List<IntegratedBoardDTO> getIntegratedBoardList(int startRow, int endRow, String boardType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        paramMap.put("boardType", boardType);
        return mapper.getIntegratedBoardList(paramMap);
    }
}
