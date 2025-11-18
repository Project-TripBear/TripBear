package com.project.trip.allplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 관광지의 상세 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblTouristSpot` 테이블과 매핑됩니다.
 */
@Data
@NoArgsConstructor
public class TouristSpotDTO {

    /**
     * 이 상세 정보가 속한 장소의 고유 식별자 (FK, tblPlace 참조)
     */
    private long placeId;

    /**
     * 관광지에 대한 개요 또는 추가 정보
     */
    private String spotOverinfo;

    /**
     * 입장료 정보
     */
    private String admissionFee;

    /**
     * 영업 또는 이용 시간
     */
    private String openingHours;

    /**
     * 문의 및 안내 전화번호
     */
    private String contactInfo;

    /**
     * 주차 시설 정보
     */
    private String parkingInfo;

    /**
     * 쉬는 날 정보
     */
    private String restDay;
}