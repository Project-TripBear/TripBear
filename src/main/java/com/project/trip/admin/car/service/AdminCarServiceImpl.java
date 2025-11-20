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

/**
 * {@link AdminCarService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AdminCarMapper}를 통해 데이터베이스와 연동하여 관리자 페이지의 렌터카 관리
 * (목록 조회, 등록, 수정, 삭제 등) 관련 비즈니스 로직을 처리합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AdminCarServiceImpl implements AdminCarService {

    private final AdminCarMapper carMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<carDTO> getAllCars(String[] fuelTypes, int minPrice, int maxPrice, String sortOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("fuelTypes", fuelTypes);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("sortOrder", sortOrder);
        return carMapper.getAllCars(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxPrice() {
        return carMapper.getMaxPrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public carDTO getCarDetail(int carId) {
        return carMapper.selectCarDetail(carId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * 신규 렌터카 등록 시, 지역 ID의 기본값을 설정한 후 데이터베이스에 삽입합니다.
     * </p>
     */
    @Override
    @Transactional
    public int addCar(carDTO dto) {
        
        // ★★★ [핵심] ★★★
        // 폼(addcar.jsp)에서 지역 ID를 받지 않으므로,
        // 형님이 주신 데이터(1: 서울)를 기반으로 기본값을 강제 설정합니다.
        dto.setPlaceLocationId(1); 
        
        return carMapper.insertCar(dto);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * </p>
     */
    @Override
    @Transactional
    public int editCar(carDTO dto) {
        // 수정(edit) 시에는 폼에서 placeLocationId가 넘어온다고 가정하므로
        // 여기서는 기본값을 설정하지 않습니다.
        return carMapper.updateCar(dto);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * </p>
     */
    @Override
    @Transactional
    public int deleteCar(int carId) {
        return carMapper.deleteCar(carId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> getAllLocations() {
        return carMapper.getAllLocations();
    }	
}