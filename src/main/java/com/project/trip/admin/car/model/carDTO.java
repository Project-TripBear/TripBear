package com.project.trip.admin.car.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 렌터카 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 차량 ID, 이름, 유형, 연료, 일일 대여료, 예약 가능 여부, 지역, 차량 번호,
 * 좌석 수, 이미지, 상태 등 렌터카 관련 정보를 포함합니다.
 * </p>
 */
@Getter
@Setter
@ToString
public class carDTO {
    /**
     * 차량의 고유 식별자
     */
    private int carId;
    /**
     * 차량의 모델명
     */
    private String carName;
    /**
     * 차량의 유형 (예: 경차, 소형, 중형, SUV)
     */
    private String carType;
    /**
     * 차량의 연료 유형 (예: 가솔린, 디젤, 전기)
     */
    private String fuelType;
    /**
     * 차량의 1일당 대여 가격
     */
    private int pricePerDay;
    /**
     * 해당 차량의 예약 가능 여부 (데이터베이스에는 없는, 로직 처리를 위한 필드)
     */
    private boolean isReserved;
 
    /**
     * 차량이 소속된 지역의 고유 식별자
     */
    private int placeLocationId;
    /**
     * 차량 번호
     */
    private String carNumber;
    /**
     * 차량의 좌석 수
     */
    private int carSeats;
    
    /**
     * 차량의 대표 이미지 URL
     */
    private String carImage;
    /**
     * 차량의 현재 상태 (예: 'Y' - 대여가능, 'N' - 대여불가)
     */
    private String carStatus;
    
    /**
     * (수정 시 사용) 기존 이미지 파일명
     */
    private String originImage;

}