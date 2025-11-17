package com.project.trip.AI.mapper;

import java.util.List;

import com.project.trip.AI.model.HealthCareLogDTO;
import com.project.trip.AI.model.RouteDTO;
import com.project.trip.AI.model.RouteStopDTO;

/**
 * AI 추천 경로 및 헬스케어 로그와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * AI 경로 및 경유지 삽입, 경로 및 경유지 조회, 헬스케어 로그 삽입 등
 * AI 기반 여행 서비스의 핵심 데이터 관리를 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
public interface AiMapper {

	/**
	 * 새로운 AI 여행 경로를 데이터베이스에 삽입합니다.
	 * @param route 삽입할 AI 여행 경로 정보가 담긴 {@link RouteDTO} 객체
	 */
	void insertAiRoute(RouteDTO route);

	/**
	 * AI 여행 경로의 새로운 경유지를 데이터베이스에 삽입합니다.
	 * @param stop 삽입할 AI 여행 경로 경유지 정보가 담긴 {@link RouteStopDTO} 객체
	 */
	void insertAiRouteStop(RouteStopDTO stop);

	/**
	 * 특정 AI 여행 경로 ID에 해당하는 경로 정보를 조회합니다.
	 * @param aiRouteId 조회할 AI 여행 경로의 고유 ID
	 * @return AI 여행 경로 정보를 담은 {@link RouteDTO} 객체
	 */
	RouteDTO findRouteById(long aiRouteId);

	/**
	 * 특정 AI 여행 경로 ID에 해당하는 모든 경유지 목록을 조회합니다.
	 * @param aiRouteId 경유지 목록을 조회할 AI 여행 경로의 고유 ID
	 * @return {@link RouteStopDTO} 객체 리스트
	 */
	List<RouteStopDTO> findStopsByRouteId(long aiRouteId);

	/**
	 * 새로운 헬스케어 로그를 데이터베이스에 삽입합니다.
	 * @param logDTO 삽입할 헬스케어 로그 정보가 담긴 {@link HealthCareLogDTO} 객체
	 */
	void insertHealthCareLog(HealthCareLogDTO logDTO);


}
