// 파일 경로: com.project.trip.admin.car.service.AdminCarServiceImpl.java

package com.project.trip.admin.car.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.admin.car.model.carDTO;
import com.project.trip.admin.car.mapper.AdminCarMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {

    private final AdminCarMapper carMapper;

    @Override
    public List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("fuelTypes", fuelTypes);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("sortOrder", sortOrder);
        return carMapper.getAllCars(params);
    }

    @Override
    public int getMaxPrice() {
        return carMapper.getMaxPrice();
    }

    @Override
    public carDTO getCarDetail(int carId) {
        return carMapper.selectCarDetail(carId);
    }

    /** 렌터카 신규 등록 */
    @Override
    @Transactional
    public int addCar(carDTO dto) {
        
        // ★★★ [핵심] ★★★
        // 폼(addcar.jsp)에서 지역 ID를 받지 않으므로,
        // 형님이 주신 데이터(1: 서울)를 기반으로 기본값을 강제 설정합니다.
        dto.setPlaceLocationId(1); 
        
        return carMapper.insertCar(dto);
    }

    /** 렌터카 정보 수정 */
    @Override
    @Transactional
    public int editCar(carDTO dto) {
        // 수정(edit) 시에는 폼에서 placeLocationId가 넘어온다고 가정하므로
        // 여기서는 기본값을 설정하지 않습니다.
        return carMapper.updateCar(dto);
    }

    /** 렌터카 삭제 */
    @Override
    @Transactional
    public int deleteCar(int carId) {
        return carMapper.deleteCar(carId);
    }
    @Override
    public List<Map<String, Object>> getAllLocations() {
        return carMapper.getAllLocations();
    }	
}