package com.project.trip.AI.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AI가 생성한 경로를 사용자가 자신의 경로로 저장하는 기능과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * AI 경로를 사용자 경로로 삽입하고, 최신 사용자 경로 ID를 조회하며,
 * AI 경로의 경유지들을 사용자 경로의 경유지로 삽입하는 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
@Mapper
public interface AiRouteSaveMapper {

    /**
     * AI가 생성한 특정 경로를 사용자의 경로로 저장합니다.
     * <p>
     * AI 경로의 기본 정보를 복사하여 새로운 사용자 경로를 생성합니다.
     * </p>
     * @param aiRouteId 저장할 AI 여행 경로의 고유 ID
     * @param userId 경로를 저장하는 사용자의 고유 ID
     * @return 삽입된 행의 수
     */
    int insertUserRouteFromAi(@Param("aiRouteId") Long aiRouteId,
                              @Param("userId") Long userId);

    /**
     * 특정 사용자가 가장 최근에 저장한 사용자 경로의 ID를 조회합니다.
     * <p>
     * 새로운 사용자 경로의 경유지를 삽입하기 위해 새로 생성된 사용자 경로의 ID를 얻을 때 사용됩니다.
     * </p>
     * @param userId 사용자 경로를 조회할 사용자의 고유 ID
     * @return 최신 사용자 경로의 고유 ID
     */
    Long getLatestUserRouteId(@Param("userId") Long userId);

    /**
     * AI가 생성한 특정 경로의 모든 경유지들을 새로운 사용자 경로의 경유지로 삽입합니다.
     * @param aiRouteId 원본 AI 여행 경로의 고유 ID
     * @param newUserRouteId 새로 생성된 사용자 여행 경로의 고유 ID
     * @return 삽입된 행의 수
     */
    int insertUserRouteStopsFromAi(@Param("aiRouteId") Long aiRouteId,
                                   @Param("newUserRouteId") Long newUserRouteId);
}
