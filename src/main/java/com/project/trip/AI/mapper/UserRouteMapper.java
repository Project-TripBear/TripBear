package com.project.trip.AI.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.project.trip.AI.model.UserRouteDTO;
import com.project.trip.AI.model.UserRouteStopDTO;

/**
 * 사용자가 저장한 여행 경로와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 사용자 경로 및 경유지 조회, 삭제, 경유지 순서 및 교통수단 업데이트,
 * 경유지 일차 및 순서 업데이트 등 사용자 정의 여행 경로 관리를 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
public interface UserRouteMapper {

    /**
     * 특정 사용자 경로 ID에 해당하는 사용자 경로 정보를 조회합니다.
     * @param userRouteId 조회할 사용자 경로의 고유 ID
     * @return 사용자 경로 정보를 담은 {@link UserRouteDTO} 객체
     */
    UserRouteDTO selectUserRouteById(Long userRouteId);

    /**
     * 특정 사용자 경로 ID에 해당하는 모든 경유지 목록을 조회합니다.
     * @param userRouteId 경유지 목록을 조회할 사용자 경로의 고유 ID
     * @return {@link UserRouteStopDTO} 객체 리스트
     */
    List<UserRouteStopDTO> selectStopsByUserRouteId(Long userRouteId);

    /**
     * 특정 사용자 경로에 속한 모든 경유지들을 삭제합니다.
     * @param userRouteId 경유지들을 삭제할 사용자 경로의 고유 ID
     * @return 삭제된 행의 수
     */
    int deleteStopsByUserRouteId(Long userRouteId);

    /**
     * 특정 사용자 경로를 삭제합니다.
     * @param userRouteId 삭제할 사용자 경로의 고유 ID
     * @return 삭제된 행의 수
     */
    int deleteUserRouteById(Long userRouteId);

    /**
     * 특정 경유지의 순서와 일차를 업데이트합니다.
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param order 경유지의 새로운 순서
     * @param day 경유지의 새로운 일차
     * @return 업데이트된 행의 수
     */
    int updateStopOrder(@Param("stopId") Long stopId,
            @Param("order") int order,
            @Param("day") int day);


    /**
     * 특정 경유지의 교통수단을 업데이트합니다.
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param mode 새로운 교통수단 (예: "CAR", "WALK")
     * @return 업데이트된 행의 수
     */
    int updateTransportMode(
            @Param("stopId") Long stopId,
            @Param("mode") String mode
   
    	);
    
    /**
     * 특정 경유지의 일차를 업데이트합니다.
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param day 새로운 일차
     * @return 업데이트된 행의 수
     */
    int updateStopDay(@Param("stopId") Long stopId, @Param("day") int day);

    /**
     * 특정 경유지의 순서를 업데이트합니다.
     * @param stopId 업데이트할 경유지의 고유 ID
     * @param order 새로운 순서
     * @return 업데이트된 행의 수
     */
    int updateOrder(@Param("stopId") Long stopId, @Param("order") int order);


}
