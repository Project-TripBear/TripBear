package com.project.trip.reservation.model;

import lombok.Data;

/**
 * 숙소 객실 정보를 카드 형태로 표시하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 숙소 선택 페이지에서 각 객실의 주요 정보를 간략하게 보여주기 위해 사용됩니다.
 * </p>
 */
@Data
public class AccomRoomCardDTO {
	
	/**
	 * 객실 고유 번호
	 */
	private Long roomId;
    /**
     * 숙소 고유 번호
     */
    private Long accomId;
    /**
     * 숙소 이름
     */
    private String accomName;
    /**
     * 객실 이름
     */
    private String roomName;
    /**
     * 1박당 가격
     */
    private Long pricePerNight;
    /**
     * 숙소 주소
     */
    private String address;
    /**
     * 숙소 대표 이미지 URL
     */
    private String imageUrl;
    
    /**
     * 숙소 위도
     */
    private Double lat;
    /**
     * 숙소 경도
     */
    private Double lng;

}
