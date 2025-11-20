package com.project.trip.AI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.AiRouteSaveMapper;

/**
 * {@link AiRouteSaveService} 인터페이스의 구현 클래스입니다.
 * <p>
 * AI가 생성한 여행 경로를 사용자가 자신의 경로로 저장하는 비즈니스 로직을 처리합니다.
 * AI 경로의 기본 정보와 경유지 정보를 복사하여 새로운 사용자 경로를 생성하고 저장하는 과정을
 * 트랜잭션으로 관리하여 데이터의 일관성을 보장합니다.
 * </p>
 */
@Service
public class AiRouteSaveServiceImpl implements AiRouteSaveService {

    @Autowired
    private AiRouteSaveMapper mapper;

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional(rollbackFor = Exception.class)}로 관리됩니다.
     * AI 경로의 기본 정보를 사용자 경로로 복사하고, 새로 생성된 사용자 경로의 ID를 조회한 후,
     * AI 경로의 경유지들을 해당 사용자 경로의 경유지로 복사합니다.
     * </p>
     * @param aiRouteId 저장할 AI 여행 경로의 고유 ID
     * @param userId 경로를 저장하는 사용자의 고유 ID
     * @return 새로 저장된 사용자 경로의 고유 ID
     * @throws IllegalArgumentException {@code aiRouteId} 또는 {@code userId}가 null인 경우
     * @throws IllegalStateException AI 루트 복사 또는 새 사용자 루트 ID 조회 실패 시
     * @throws Exception 데이터베이스 작업 중 발생할 수 있는 예외
     */
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
