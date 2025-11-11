// 파일 경로: com.project.trip.admin.car.mapper.AdminCarMapper.java

package com.project.trip.admin.car.mapper;

import java.util.List;
import java.util.Map;

import com.project.trip.admin.car.model.carDTO;

public interface AdminCarMapper {

	void addCar(carDTO dto);
    
    // 1. [추가] 최대 가격
    int getMaxPrice();

    // 2. [추가] 렌터카 목록
    List<carDTO> getAllCars(Map<String, Object> params);
    
    // 3. [추가] 렌터카 삭제
    void deleteCar(int carId);
}