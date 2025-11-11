package com.project.trip.admin.car.mapper;

import java.util.List;
import java.util.Map;
import com.project.trip.admin.car.model.carDTO;

public interface AdminCarMapper {

    int insertCar(carDTO dto);

    int getMaxPrice();

    List<carDTO> getAllCars(Map<String, Object> params);

    carDTO selectCarDetail(int carId); // 상세 조회 추가

    int deleteCar(int carId); // 리턴 타입 int로 변경

    int updateCar(carDTO dto);
}
