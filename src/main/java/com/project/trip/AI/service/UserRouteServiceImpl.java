package com.project.trip.AI.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.AI.mapper.UserRouteMapper;
import com.project.trip.AI.model.StopOrderDTO;
import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.model.UserRouteStopDTO;

import lombok.RequiredArgsConstructor;

/**
 * {@link UserRouteService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link UserRouteMapper}를 통해 데이터베이스와 연동하여 사용자가 저장한 여행 경로(내 여행)와 관련된
 * 비즈니스 로직을 처리합니다. 사용자 경로 조회, 삭제, 경유지 순서 및 교통수단 변경, 일차 변경 등
 * 사용자 정의 여행 경로 관리를 위한 기능을 제공합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserRouteServiceImpl implements UserRouteService {

    private final UserRouteMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRouteDTO getUserRouteWithStops(Long userRouteId) {
        UserRouteDTO route = mapper.selectUserRouteById(userRouteId);
        if (route == null) return null;
        List<UserRouteStopDTO> stops = mapper.selectStopsByUserRouteId(userRouteId);
        route.setStops(stops);
        return route;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * 사용자 경로와 관련된 모든 경유지 정보를 먼저 삭제한 후, 사용자 경로 자체를 삭제합니다.
     * </p>
     */
    @Override
    @Transactional
    public int deleteUserRouteCascade(Long userRouteId) {
        mapper.deleteStopsByUserRouteId(userRouteId);
        return mapper.deleteUserRouteById(userRouteId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int updateTransportMode(Long stopId, String mode) {
        return mapper.updateTransportMode(stopId, mode);
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * </p>
     */
    @Transactional
    @Override
    public int updateStopOrder(Long stopId, int day, int order) {
        return mapper.updateStopOrder(stopId, order, day);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * </p>
     */
    @Transactional
    @Override
    public void updateStopOrders(int day, List<StopOrderDTO> stops) {
        for (StopOrderDTO s : stops) {
            mapper.updateStopOrder(s.getStopId(), s.getOrder(), day);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int updateStopDay(Long stopId, int day) {
        return mapper.updateStopDay(stopId, day);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateOrder(Long stopId, int order) {
        return mapper.updateOrder(stopId, order);
    }

    
}
