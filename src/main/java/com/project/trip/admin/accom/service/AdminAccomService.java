// 파일 경로: com.project.trip.admin.accom.service.AdminAccomService.java

package com.project.trip.admin.accom.service;

import java.util.List;
import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;

/**
 * 관리자 페이지의 숙소 관리와 관련된 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface AdminAccomService {

    /**
     * 등록된 모든 숙소 중 가장 높은 1박 가격을 조회합니다.
     * @return 최고 가격
     */
    int getMaxPrice();

    /**
     * 필터링 및 정렬 조건에 따라 숙소 목록을 조회합니다.
     * @param accomTypes 숙소 유형 배열 (예: ["호텔", "펜션"])
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @param sortOrder 정렬 순서 (예: "price_asc", "price_desc")
     * @return {@link accomDTO} 객체 리스트
     */
    List<accomDTO> getAllAccommodations(String[] accomTypes, int minPrice, int maxPrice, String sortOrder);
    
    /**
     * 신규 숙소를 등록합니다.
     * <p>
     * 숙소 정보는 여러 테이블에 걸쳐 저장되므로, 이 작업은 단일 트랜잭션으로 처리되어야 합니다.
     * </p>
     * @param dto 등록할 숙소의 모든 정보를 담은 {@link accomAllInfoDTO} 객체
     */
    void addAccommodation(accomAllInfoDTO dto);
    
    /**
     * 특정 객실 ID에 해당하는 숙소의 모든 상세 정보를 조회합니다.
     * <p>
     * 주로 숙소 정보 수정 페이지에서 기존 데이터를 불러오는 데 사용됩니다.
     * </p>
     * @param roomId 조회할 객실의 고유 ID
     * @return 숙소의 모든 정보를 담은 {@link accomAllInfoDTO} 객체
     */
    accomAllInfoDTO getAccommodationDetails(int roomId);
    
    /**
     * 숙소 정보를 수정합니다.
     * <p>
     * 숙소 정보는 여러 테이블에 걸쳐 저장되므로, 이 작업은 단일 트랜잭션으로 처리되어야 합니다.
     * </p>
     * @param dto 수정할 숙소의 모든 정보를 담은 {@link accomAllInfoDTO} 객체
     */
    void updateAccommodation(accomAllInfoDTO dto);
    
    /**
     * 숙소 정보를 삭제합니다.
     * <p>
     * 객실 정보와 함께 연관된 숙소, 장소 정보를 모두 삭제하며,
     * 이 작업은 단일 트랜잭션으로 처리되어야 합니다.
     * </p>
     * @param roomId 삭제할 객실의 고유 ID
     */
    void deleteAccommodation(int roomId);
}	