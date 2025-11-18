package com.project.trip.AI.service;

import java.util.List;

import com.project.trip.AI.model.StopOrderDTO;
import com.project.trip.AI.model.UserRouteDTO;

/**
 * 사용자가 저장한 여행 경로(내 여행)와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 * <p>
 * 사용자 경로 조회, 삭제, 경유지 순서 및 교통수단 변경, 일차 변경 등
 * 사용자 정의 여행 경로 관리를 위한 기능을 제공합니다.
 * </p>
 */
public interface UserRouteService {

    /**
     * 특정 사용자 여행 경로의 상세 정보와 모든 경유지 목록을 함께 조회합니다.
     *
     * @param userRouteId 조회할 사용자 여행 경로의 고유 ID
     * @return 사용자 여행 경로의 전체 정보를 담은 {@link UserRouteDTO} 객체
     */
    UserRouteDTO getUserRouteWithStops(Long userRouteId);

    /**
     * 특정 사용자 여행 경로와 관련된 모든 경유지 정보를 함께 삭제합니다.
     *
     * @param userRouteId 삭제할 사용자 여행 경로의 고유 ID
     * @return 삭제된 행의 수
     */
    int deleteUserRouteCascade(Long userRouteId);

    /**
     * 특정 경유지의 교통수단을 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param mode 새로운 교통수단 (예: "CAR", "WALK")
     * @return 업데이트된 행의 수
     */
    int updateTransportMode(Long stopId, String mode);

    /**
     * 특정 경유지의 순서와 일차를 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param day 새로운 일차 값
     * @param order 새로운 순서 값
     * @return 업데이트된 행의 수
     */
    int updateStopOrder(Long stopId, int day, int order);
    /**
     * 특정 일차에 해당하는 경유지들의 순서를 일괄적으로 업데이트합니다.
     *
     * @param day 업데이트할 경유지들의 일차
     * @param stops 업데이트할 경유지 ID와 순서 정보를 담은 {@code List<StopOrderDTO>}
     */
    void updateStopOrders(int day, List<StopOrderDTO> stops);
    
    /**
     * 특정 경유지의 일차를 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param day 새로운 일차 값
     * @return 업데이트된 행의 수
     */
    int updateStopDay(Long stopId, int day);
    /**
     * 특정 경유지의 순서를 업데이트합니다.
     *
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param order 새로운 순서 값
     * @return 업데이트된 행의 수
     */
    int updateOrder(Long stopId, int order);

    
}
