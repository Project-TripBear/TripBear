// 파일 경로: com.project.trip.admin.model.accomDTO.java
package com.project.trip.admin.accom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 숙소 및 객실의 기본 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 객실 ID, 숙소 이름, 숙소 유형, 객실 이름, 1박 가격, 수용 인원, 객실 이미지, 예약 가능 여부 등의 정보를 포함합니다.
 * </p>
 */
@Getter
@Setter
@ToString
public class accomDTO {
    /**
     * 객실의 고유 식별자
     */
    private int roomId;
    /**
     * 숙소의 이름
     */
    private String accomName;
    /**
     * 숙소의 유형 (예: 호텔, 펜션, 게스트하우스)
     */
    private String accomType;
    /**
     * 객실의 이름
     */
    private String roomName;
    /**
     * 객실의 1박당 가격
     */
    private int pricePerNight;
    /**
     * 객실의 최대 수용 인원
     */
    private int capacity;
    
    /**
     * 객실의 대표 이미지 URL
     */
    private String roomImage; 
    
    /**
     * 해당 객실의 예약 가능 여부 (데이터베이스에는 없는, 로직 처리를 위한 필드)
     */
    private boolean isReserved;
}