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

/**
 * {@link AdminAccomService} 인터페이스의 구현 클래스입니다.
 * <p>
 * {@link AdminAccomMapper}를 통해 데이터베이스와 연동하여 관리자 페이지의 숙소 관리
 * (목록 조회, 등록, 수정, 삭제 등) 관련 비즈니스 로직을 처리합니다.
 * 숙소 등록, 수정, 삭제와 같이 여러 테이블에 걸친 작업은 트랜잭션으로 처리하여
 * 데이터의 일관성을 보장합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AdminAccomServiceImpl implements AdminAccomService {

    private final AdminAccomMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxPrice() {
        return mapper.getMaxPrice();
    }

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * Place, Accom, AccomRoom 순서로 데이터를 삽입하며, 모든 과정이 성공해야 커밋됩니다.
     * 현재는 주소에 대한 좌표 값을 임시로 사용하고 있으며, 추후 지오코딩 API 연동이 필요합니다.
     * </p>
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public accomAllInfoDTO getAccommodationDetails(int roomId) {
        // 3개 테이블을 조인한 상세 쿼리 호출
        return mapper.getAccommodationDetails(roomId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * Place, Accom, AccomRoom 순서로 데이터를 업데이트하며, 모든 과정이 성공해야 커밋됩니다.
     * </p>
     */
    @Override
    @Transactional
    public void updateAccommodation(accomAllInfoDTO dto) {
        // DTO에 모든 ID(placeId, accomId, roomId)가 있다고 가정하고
        // 3개 테이블을 순차적으로 업데이트
    	
    	dto.setPlaceLat(37.5665); 
        dto.setPlaceLng(126.9780);
        
        dto.setPlaceLocationId(1); // (임시 값 1 - '서울'이라고 가정)
        dto.setPlaceTypeId(1);
        
        
        mapper.updatePlace(dto);
        mapper.updateAccom(dto);
        mapper.updateAccomRoom(dto);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 이 메소드는 {@code @Transactional}로 관리됩니다.
     * 먼저 객실 ID를 통해 연관된 숙소 ID와 장소 ID를 조회한 후,
     * 외래 키 제약조건을 위배하지 않도록 Room, Accom, Place 순서로 데이터를 삭제합니다.
     * </p>
     */
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