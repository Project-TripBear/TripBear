// 파일 경로: com.project.trip.admin.car.service.AdminCarService.java

package com.project.trip.admin.car.service;

import java.util.List;
import java.util.Map;

import com.project.trip.admin.car.model.carDTO;

/**
 * 관리자 페이지의 렌터카 관리와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AdminCarService {

	/**
     * 필터링 및 정렬 조건에 따라 렌터카 목록을 조회합니다.
     * @param fuelTypes 연료 유형 배열 (예: ["가솔린", "디젤"])
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param sortOrder 정렬 순서 (예: "price_asc", "price_desc")
     * @return {@link carDTO} 객체 리스트
     */
    List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder);

    /**
     * 등록된 모든 렌터카 중 가장 높은 1일 대여료를 조회합니다.
     * @return 최고 가격
     */
    int getMaxPrice();

    /**
     * 특정 렌터카 ID에 해당하는 상세 정보를 조회합니다.
     * @param carId 조회할 렌터카의 고유 ID
     * @return 렌터카 상세 정보를 담은 {@link carDTO} 객체
     */
    carDTO getCarDetail(int carId);

    /**
     * 신규 렌터카를 등록합니다.
     * @param dto 등록할 렌터카 정보를 담은 {@link carDTO} 객체
     * @return 등록 성공 시 1, 실패 시 0
     */
    int addCar(carDTO dto);

    /**
     * 렌터카 정보를 수정합니다.
     * @param dto 수정할 렌터카 정보를 담은 {@link carDTO} 객체
     * @return 수정 성공 시 1, 실패 시 0
     */
    int editCar(carDTO dto);

    /**
     * 렌터카 정보를 삭제합니다.
     * @param carId 삭제할 렌터카의 고유 ID
     * @return 삭제 성공 시 1, 실패 시 0
     */
    int deleteCar(int carId);
    
    /**
     * 모든 지역 목록을 조회합니다.
     * @return 각 지역의 ID와 이름을 담은 Map 객체의 리스트
     */
    List<Map<String, Object>> getAllLocations();
}