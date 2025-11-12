package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.AiRouteSaveMapper;

@Service
public class AiRouteSaveServiceImpl implements AiRouteSaveService {

    @Autowired
    private AiRouteSaveMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUserRouteFromAi(Long aiRouteId, Long userId) throws Exception {

        if (aiRouteId == null || userId == null) {
            throw new IllegalArgumentException("aiRouteId 또는 userId가 null입니다.");
        }

        // ① 사용자 루트 정보 복사
        int inserted = mapper.insertUserRouteFromAi(aiRouteId, userId);
        if (inserted == 0) {
            throw new IllegalStateException("AI 루트 복사 실패");
        }

        // ② 새 user_route_id 조회
        Long newUserRouteId = mapper.getLatestUserRouteId(userId);
        if (newUserRouteId == null) {
            throw new IllegalStateException("새 사용자 루트 ID 조회 실패");
        }

        // ③ 해당 루트의 스탑 복사
        mapper.insertUserRouteStopsFromAi(aiRouteId, newUserRouteId);

        return newUserRouteId;
    }
}
