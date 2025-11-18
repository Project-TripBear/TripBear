package com.project.trip.reservation.model;

import lombok.Data;

/**
 * 렌터카 정보를 카드 형태로 표시하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 렌터카 선택 페이지에서 각 차량의 주요 정보를 간략하게 보여주기 위해 사용됩니다.
 * </p>
 */
@Data
public class RentalCarCardDTO {
	/**
	 * 렌터카 고유 번호
	 */
	private Long carId;
    /**
     * 장소 지역 ID
     */
    private Long placeLocationId;
    /**
     * 차량 이름 (모델명)
     */
    private String carName;
    /**
     * 차량 타입 (예: 경차, 소형, 중형)
     */
    private String carType;
    /**
     * 차량 번호
     */
    private String carNumber;
    /**
     * 연료 타입 (예: 휘발유, 경유, 전기)
     */
    private String fuelType;
    /**
     * 좌석 수
     */
    private int seats;
    /**
     * 1일당 가격
     */
    private int pricePerDay;
    /**
     * 차량 이미지 URL
     */
    private String carImageUrl;
}
