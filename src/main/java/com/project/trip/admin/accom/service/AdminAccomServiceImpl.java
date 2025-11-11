// 파일 경로: com.project.trip.admin.accom.service.AdminAccomServiceImpl.java

package com.project.trip.admin.accom.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.admin.accom.mapper.AdminAccomMapper;
import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAccomServiceImpl implements AdminAccomService {

    private final AdminAccomMapper mapper;

    @Override
    public int getMaxPrice() {
        return mapper.getMaxPrice();
    }

    @Override
    public List<accomDTO> getAllAccommodations(String[] accomTypes, int minPrice, int maxPrice, String sortOrder) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("accomTypes", accomTypes);
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("sortOrder", sortOrder);
        
        return mapper.getAllAccommodations(params);
    }

    /**
     * 숙소 등록 (Place -> Accom -> AccomRoom 순서로 INSERT)
     * 3개의 INSERT가 모두 성공해야 하므로 @Transactional 처리
     */
    @Override
    @Transactional
    public void addAccommodation(accomAllInfoDTO dto) {
        
    	// ★★★ 나중에 수정할 수 있도록 주석을 남깁니다. ★★★
        // TODO: [지오코딩 API 연동] 현재는 NOT NULL 제약조건 통과를 위해 임시 좌표(서울시청)를 사용함.
        // 추후 카카오/네이버 Geocoding API를 연동하여 dto.getPlaceAddress() 값으로 실제 좌표를 받아와야 함.
        dto.setPlaceLat(37.5665); 
        dto.setPlaceLng(126.9780);
        
        dto.setPlaceLocationId(1); // (임시 값 1 - '서울'이라고 가정)
        dto.setPlaceTypeId(1);
    
        mapper.insertPlace(dto); // 이 때 DTO의 placeId가 채워집니다.

        
        mapper.insertAccom(dto); // 이 때 DTO의 accomId가 채워집니다.
        
      
        mapper.insertAccomRoom(dto);
    }
    
    @Override
    public accomAllInfoDTO getAccommodationDetails(int roomId) {
        // 3개 테이블을 조인한 상세 쿼리 호출
        return mapper.getAccommodationDetails(roomId);
    }

    @Override
    @Transactional
    public void updateAccommodation(accomAllInfoDTO dto) {
        // DTO에 모든 ID(placeId, accomId, roomId)가 있다고 가정하고
        // 3개 테이블을 순차적으로 업데이트
        mapper.updatePlace(dto);
        mapper.updateAccom(dto);
        mapper.updateAccomRoom(dto);
    }

    @Override
    @Transactional
    public void deleteAccommodation(int roomId) {
        // 1. roomId로 accomId와 placeId를 먼저 조회합니다.
        accomAllInfoDTO ids = mapper.getAccomIdAndPlaceIdByRoomId(roomId);
        
        if (ids != null) {
            // 2. FK 역순으로 삭제: Room -> Accom -> Place
            mapper.deleteAccomRoom(roomId);
            mapper.deleteAccom(ids.getAccomId());
            mapper.deletePlace(ids.getPlaceId());
        }
    }    
    
}