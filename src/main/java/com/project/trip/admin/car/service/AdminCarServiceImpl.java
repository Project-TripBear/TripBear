// 파일 경로: com.project.trip.admin.car.service.AdminCarServiceImpl.java

package com.project.trip.admin.car.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.trip.admin.car.mapper.AdminCarMapper;
import com.project.trip.admin.car.model.carDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {

    private final AdminCarMapper mapper;

    @Override
    public void addCar(carDTO dto) {
    	// ★★★ [추가] DDL NOT NULL 제약조건(place_Location_id)을 위한 임시 값 설정 ★★★
        // TODO: [데이터 자동화] 나중에 '지역' 선택 폼을 추가하여 이 값을 동적으로 받아야 함.
        dto.setPlaceLocationId(1); // (임시 값 1)
        
        mapper.addCar(dto);
    
    }
    
    @Override
    public int getMaxPrice() {
        return mapper.getMaxPrice();
    }

    @Override
    public List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("fuelTypes", fuelTypes);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("sortOrder", sortOrder);
        
        return mapper.getAllCars(params);
    }

    @Override
    public void deleteCar(int carId) {
        // TODO: tblCarReservation의 예약 내역을 먼저 삭제하는 로직 필요
        mapper.deleteCar(carId);
    }
}	