package com.project.trip.AI.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

/**
 * AI가 생성한 여행 경로를 조회하는 기능과 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * AI 경로의 기본 정보 조회, 경로에 포함된 경유지 목록 조회, 특정 경유지의 상세 정보 조회 등
 * AI 기반 여행 경로 시각화 및 정보 제공을 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
public interface AiRouteViewMapper {

    /**
     * 특정 AI 여행 경로의 기본 정보를 조회합니다.
     * @param aiRouteId 조회할 AI 여행 경로의 고유 ID
     * @return AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
     */
    RouteDTO getAiRoute(long aiRouteId);

    /**
     * 특정 AI 여행 경로에 포함된 모든 경유지 목록을 조회합니다.
     * @param aiRouteId 경유지 목록을 조회할 AI 여행 경로의 고유 ID
     * @return {@link RouteStopDTO} 객체 리스트
     */
    List<RouteStopDTO> getStops(long aiRouteId);

    /**
     * 특정 AI 여행 경로 경유지의 상세 정보를 조회합니다.
     * @param aiRouteStopId 조회할 AI 여행 경로 경유지의 고유 ID
     * @return 경유지 상세 정보를 담은 {@link RouteStopDTO} 객체
     */
    RouteStopDTO getStopDetail(long aiRouteStopId);
}
