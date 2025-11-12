// 파일 경로: com.project.trip.admin.accom.service.AdminAccomService.java

package com.project.trip.admin.accom.service;

import java.util.List;
import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;

public interface AdminAccomService {

    // 1. DB의 1박 요금 최대값 조회
    int getMaxPrice();

    // 2. 필터링/정렬된 숙소 목록 조회
    List<accomDTO> getAllAccommodations(String[] accomTypes, int minPrice, int maxPrice, String sortOrder);
    
    // 3. 신규 숙소 등록 (트랜잭션)
    void addAccommodation(accomAllInfoDTO dto);
    
 // 4. [추가] 숙소 상세 정보 조회 (수정 페이지용)
    accomAllInfoDTO getAccommodationDetails(int roomId);
    
    // 5. [추가] 숙소 정보 수정 (트랜잭션)
    void updateAccommodation(accomAllInfoDTO dto);
    
    // 6. [추가] 숙소 정보 삭제 (트랜잭션)
    void deleteAccommodation(int roomId);
}	