package com.project.trip.admin.car.service;

import java.util.Arrays;
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

    /** 렌터카 목록 조회 (필터링 포함) */
    @Override
    public List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder) {
        // 여러 개의 파라미터를 Map으로 변환하여 Mapper에 전달
        Map<String, Object> params = new HashMap<>();
        params.put("fuelTypes", fuelTypes);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("sortOrder", sortOrder);

        // Mapper 메서드 호출
        return carMapper.getAllCars(params);
    }

    /** DB에 등록된 최대 가격 조회 */
    @Override
    public int getMaxPrice() {
        return carMapper.getMaxPrice();
    }

    /** 렌터카 상세 정보 조회 */
    @Override
    public carDTO getCarDetail(int carId) {
        return carMapper.selectCarDetail(carId);
    }

    /** 렌터카 신규 등록 */
    @Override
    @Transactional
    public int addCar(carDTO dto) {
        return carMapper.insertCar(dto); // Mapper에서 정의한 insertCar 호출
    }

    /** 렌터카 정보 수정 */
    @Override
    @Transactional
    public int editCar(carDTO dto) {
        return carMapper.updateCar(dto);
    }

    /** 렌터카 삭제 */
    @Override
    @Transactional
    public int deleteCar(int carId) {
        return carMapper.deleteCar(carId);
    }
}
