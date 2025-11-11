package com.project.trip.AI.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiRouteSaveMapper {

    // AI 루트를 사용자 루트로 복사
    void insertUserRouteFromAi(@Param("aiRouteId") Long aiRouteId, @Param("userId") Long userId);

    // 새로 생성된 사용자 루트 ID 조회
    Long getLatestUserRouteId(@Param("userId") Long userId);

    // AI 루트의 스탑 → 사용자 루트 스탑 복사
    void insertUserRouteStopsFromAi(@Param("aiRouteId") Long aiRouteId,
                                    @Param("userRouteId") Long userRouteId);
}
