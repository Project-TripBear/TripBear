// 파일 경로: com.project.trip.admin.accom.mapper.AdminAccomMapper.java

package com.project.trip.admin.accom.mapper;

import java.util.List;
import java.util.Map;
import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;

public interface AdminAccomMapper {

    // 1. 숙소 목록
    List<accomDTO> getAllAccommodations(Map<String, Object> params);

    // 2. 최대 가격
    int getMaxPrice();

    // 3. 숙소 등록 (3단계)
    void insertPlace(accomAllInfoDTO dto);
    void insertAccom(accomAllInfoDTO dto);
    void insertAccomRoom(accomAllInfoDTO dto);
    
 // 4. 숙소 상세 정보 조회
    accomAllInfoDTO getAccommodationDetails(int roomId);

    // 5. 숙소 수정 (3단계)
    void updatePlace(accomAllInfoDTO dto);
    void updateAccom(accomAllInfoDTO dto);
    void updateAccomRoom(accomAllInfoDTO dto);
    
    // 6. 숙소 삭제 (3단계)
    accomAllInfoDTO getAccomIdAndPlaceIdByRoomId(int roomId); // (삭제할 ID 조회용)
    void deleteAccomRoom(int roomId);
    void deleteAccom(int accomId);
    void deletePlace(int placeId);

}