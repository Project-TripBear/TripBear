// 파일 경로: com.project.trip.admin.model.accomAllInfoDTO.java
package com.project.trip.admin.accom.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class accomAllInfoDTO {
    
    // --- DB에서 생성되는 ID ---
    private int placeId;
    private int accomId;
    private int roomId;
    
    // --- 1. tblPlace (숙소 위치) ---
    private String placeName;
    private String placeAddress;
    private double placeLat; // (위도)
    private double placeLng; // (경도)
    private int placeTypeId; // (장소 유형 ID)
    private int placeLocationId; // (지역 ID)
    private String placeMainImageUrl; // (숙소 대표 이미지 URL)
    private String placeDescription; // (숙소 설명)
    
    // --- 2. tblAccom (숙소 공통 정보) ---
    private String accomType; // (호텔, 펜션 등)
    private String accomTel; // (숙소 전화번호)

    // --- 3. tblAccomRoom (기본 객실 정보) ---
    private String roomName;
    private String roomType; // (스탠다드, 디럭스 등)
    private int capacity;
    private int pricePerNight;
    private String roomArea; // (객실 평수/크기)
    private String roomImageUrl; // (객실 대표 이미지 URL)
    private String roomStatus; // (객실 상태, y/n)
}