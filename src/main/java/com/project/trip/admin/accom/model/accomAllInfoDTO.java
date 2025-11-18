// 파일 경로: com.project.trip.admin.model.accomAllInfoDTO.java
package com.project.trip.admin.accom.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 숙소와 관련된 모든 정보를 통합하여 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 장소 정보({@code tblPlace}), 숙소 공통 정보({@code tblAccom}), 객실 정보({@code tblAccomRoom})를
 * 모두 포함하며, 주로 숙소 등록 및 수정 시 데이터를 한 번에 처리하기 위해 사용됩니다.
 * </p>
 */
@Getter
@Setter
public class accomAllInfoDTO {
    
    // --- DB에서 생성되는 ID ---
    /**
     * 장소의 고유 식별자
     */
    private int placeId;
    /**
     * 숙소의 고유 식별자
     */
    private int accomId;
    /**
     * 객실의 고유 식별자
     */
    private int roomId;
    
    // --- 1. tblPlace (숙소 위치) ---
    /**
     * 장소(숙소)의 이름
     */
    private String placeName;
    /**
     * 장소(숙소)의 주소
     */
    private String placeAddress;
    /**
     * 장소의 위도
     */
    private double placeLat;
    /**
     * 장소의 경도
     */
    private double placeLng;
    /**
     * 장소 유형의 고유 식별자
     */
    private int placeTypeId;
    /**
     * 지역의 고유 식별자
     */
    private int placeLocationId;
    /**
     * 숙소의 대표 이미지 URL
     */
    private String placeMainImageUrl;
    /**
     * 숙소에 대한 설명
     */
    private String placeDescription;
    
    // --- 2. tblAccom (숙소 공통 정보) ---
    /**
     * 숙소의 유형 (예: 호텔, 펜션)
     */
    private String accomType;
    /**
     * 숙소의 연락처(전화번호)
     */
    private String accomTel;

    // --- 3. tblAccomRoom (기본 객실 정보) ---
    /**
     * 객실의 이름
     */
    private String roomName;
    /**
     * 객실의 유형 (예: 스탠다드, 디럭스)
     */
    private String roomType;
    /**
     * 객실의 최대 수용 인원
     */
    private int capacity;
    /**
     * 객실의 1박당 가격
     */
    private int pricePerNight;
    /**
     * 객실의 면적(평수/크기)
     */
    private String roomArea;
    /**
     * 객실의 대표 이미지 URL
     */
    private String roomImageUrl;
    /**
     * 객실의 현재 상태 (예: 'Y' - 판매중, 'N' - 판매중지)
     */
    private String roomStatus;
}