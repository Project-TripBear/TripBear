// 파일 경로: com.project.trip.admin.accom.mapper.AdminAccomMapper.java

package com.project.trip.admin.accom.mapper;

import java.util.List;
import java.util.Map;
import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;

/**
 * 관리자 페이지의 숙소 관리와 관련된 데이터베이스 연동을 위한 매퍼 인터페이스입니다.
 * <p>
 * 숙소 목록 조회, 등록, 수정, 삭제 및 상세 정보 조회 등 숙소 관리를 위한
 * SQL 쿼리 호출을 정의합니다. 숙소 정보는 {@code tblPlace}, {@code tblAccom},
 * {@code tblAccomRoom} 세 개의 테이블과 연관되어 처리됩니다.
 * </p>
 */
public interface AdminAccomMapper {

    /**
     * 검색 조건에 맞는 숙소 목록을 조회합니다.
     * @param params 검색 조건을 담은 Map (예: accomType, keyword, minPrice, maxPrice)
     * @return {@link accomDTO} 객체 리스트
     */
    List<accomDTO> getAllAccommodations(Map<String, Object> params);

    /**
     * 등록된 모든 숙소 중 가장 높은 1박 가격을 조회합니다.
     * <p>
     * 가격 필터의 최대값 설정에 사용될 수 있습니다.
     * </p>
     * @return 최고 가격
     */
    int getMaxPrice();

    /**
     * 장소(Place) 정보를 데이터베이스에 삽입합니다.
     * @param dto 삽입할 장소 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void insertPlace(accomAllInfoDTO dto);
    
    /**
     * 숙소(Accom) 정보를 데이터베이스에 삽입합니다.
     * @param dto 삽입할 숙소 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void insertAccom(accomAllInfoDTO dto);
    
    /**
     * 객실(AccomRoom) 정보를 데이터베이스에 삽입합니다.
     * @param dto 삽입할 객실 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void insertAccomRoom(accomAllInfoDTO dto);
    
    /**
     * 특정 객실 ID에 해당하는 숙소의 모든 상세 정보를 조회합니다.
     * @param roomId 조회할 객실의 고유 ID
     * @return 숙소의 모든 정보를 담은 {@link accomAllInfoDTO} 객체
     */
    accomAllInfoDTO getAccommodationDetails(int roomId);

    /**
     * 장소(Place) 정보를 업데이트합니다.
     * @param dto 업데이트할 장소 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void updatePlace(accomAllInfoDTO dto);
    
    /**
     * 숙소(Accom) 정보를 업데이트합니다.
     * @param dto 업데이트할 숙소 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void updateAccom(accomAllInfoDTO dto);
    
    /**
     * 객실(AccomRoom) 정보를 업데이트합니다.
     * @param dto 업데이트할 객실 정보가 담긴 {@link accomAllInfoDTO} 객체
     */
    void updateAccomRoom(accomAllInfoDTO dto);
    
    /**
     * 객실 ID를 기반으로 연관된 숙소 ID와 장소 ID를 조회합니다.
     * <p>
     * 숙소 정보 삭제 시 연관된 모든 테이블의 데이터를 삭제하기 위해 사용됩니다.
     * </p>
     * @param roomId 조회할 객실의 고유 ID
     * @return 숙소 ID와 장소 ID를 담은 {@link accomAllInfoDTO} 객체
     */
    accomAllInfoDTO getAccomIdAndPlaceIdByRoomId(int roomId);
    
    /**
     * 특정 객실 정보를 삭제합니다.
     * @param roomId 삭제할 객실의 고유 ID
     */
    void deleteAccomRoom(int roomId);
    
    /**
     * 특정 숙소 정보를 삭제합니다.
     * @param accomId 삭제할 숙소의 고유 ID
     */
    void deleteAccom(int accomId);
    
    /**
     * 특정 장소 정보를 삭제합니다.
     * @param placeId 삭제할 장소의 고유 ID
     */
    void deletePlace(int placeId);

}