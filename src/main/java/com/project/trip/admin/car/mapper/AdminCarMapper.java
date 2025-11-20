package com.project.trip.admin.car.mapper;

import java.util.List;
import java.util.Map;
import com.project.trip.admin.car.model.carDTO;

/**
 * 관리자 페이지의 렌터카 관리와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 렌터카 목록 조회, 등록, 수정, 삭제, 상세 정보 조회 및 지역 목록 조회 등
 * 렌터카 관리를 위한 SQL 쿼리 호출을 정의합니다.
 * </p>
 */
public interface AdminCarMapper {

    /**
     * 신규 렌터카 정보를 데이터베이스에 삽입합니다.
     * @param dto 삽입할 렌터카 정보가 담긴 {@link carDTO} 객체
     * @return 삽입된 행의 수
     */
    int insertCar(carDTO dto);

    /**
     * 등록된 모든 렌터카 중 가장 높은 1일 대여료를 조회합니다.
     * <p>
     * 가격 필터의 최대값 설정에 사용될 수 있습니다.
     * </p>
     * @return 최고 가격
     */
    int getMaxPrice();

    /**
     * 검색 조건에 맞는 렌터카 목록을 조회합니다.
     * @param params 검색 조건을 담은 Map (예: carType, fuelType, minPrice, maxPrice)
     * @return {@link carDTO} 객체 리스트
     */
    List<carDTO> getAllCars(Map<String, Object> params);

    /**
     * 특정 렌터카 ID에 해당하는 상세 정보를 조회합니다.
     * @param carId 조회할 렌터카의 고유 ID
     * @return 렌터카 상세 정보를 담은 {@link carDTO} 객체
     */
    carDTO selectCarDetail(int carId);

    /**
     * 특정 렌터카 정보를 삭제합니다.
     * @param carId 삭제할 렌터카의 고유 ID
     * @return 삭제된 행의 수
     */
    int deleteCar(int carId);

    /**
     * 렌터카 정보를 업데이트합니다.
     * @param dto 업데이트할 렌터카 정보가 담긴 {@link carDTO} 객체
     * @return 업데이트된 행의 수
     */
    int updateCar(carDTO dto);
    
    /**
     * 모든 지역 목록을 조회합니다.
     * <p>
     * 렌터카 등록/수정 시 지역 선택 드롭다운에 사용될 수 있습니다.
     * </p>
     * @return 각 지역의 ID와 이름을 담은 Map 객체의 리스트
     */
    List<Map<String, Object>> getAllLocations();
}
