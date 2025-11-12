// 파일 경로: com.project.trip.admin.car.service.AdminCarService.java

package com.project.trip.admin.car.service;

import java.util.List;

import com.project.trip.admin.car.model.carDTO;

public interface AdminCarService {

	/** 렌터카 목록 조회 (필터링 포함) */
    List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder);

    /** DB에 등록된 최대 가격 조회 */
    int getMaxPrice();

    /** 렌터카 상세 정보 조회 */
    carDTO getCarDetail(int carId);

    /** 렌터카 신규 등록 */
    int addCar(carDTO dto);

    /** 렌터카 정보 수정 */
    int editCar(carDTO dto);

    /** 렌터카 삭제 */
    int deleteCar(int carId);
}