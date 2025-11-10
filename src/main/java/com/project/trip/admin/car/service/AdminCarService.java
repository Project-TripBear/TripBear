// 파일 경로: com.project.trip.admin.car.service.AdminCarService.java

package com.project.trip.admin.car.service;

import com.project.trip.admin.car.model.carDTO;

public interface AdminCarService {

    void addCar(carDTO dto);
    
    // (추후 목록, 수정, 삭제 기능 추가)
}