package com.project.trip.mypage.model;

import lombok.Data;

/**
 * 마이페이지에서 렌터카 예약 내역을 조회하기 위한 데이터 전송 객체(DTO)입니다.
 * 렌터카 예약과 관련된 다양한 정보를 포함합니다.
 */
@Data
public class CarReservationViewDTO {
	/**
	 * 예약 번호 (reservation_id)
	 */
	private String seq;             // reservation_id - 예약번호
	/**
	 * 사용자 ID (user_id)
	 */
	    private String useq;            // user_id - 사용자ID
	/**
	 * 렌트카 예약 ID (car_reservation_id)
	 */
	    private String carseq;          // car_reservation_id - 렌트카 예약ID
	/**
	 * 상태명 (status_name)
	 */
	    private String statusname;      // status_name - 상태명
	/**
	 * 대여일 (pickup_date)
	 */
	    private String pickupdate;      // pickup_date - 대여일
	/**
	 * 반납일 (dropoff_date)
	 */
	    private String dropoffdate;     // dropoff_date - 반납일
	/**
	 * 대여 지점 (pickup_location)
	 */
	    private String pickuplocation;  // pickup_location - 대여지점
	/**
	 * 반납 지점 (dropoff_location)
	 */
	    private String dropofflocation; // dropoff_location - 반납지점
	/**
	 * 렌터카 총 금액 (car_total_price)
	 */
	    private String cartotalprice;   // car_total_price - 총 금액
	/**
	 * 비고 (car_notes)
	 */
	    private String carnotes;        // car_notes - 비고
	/**
	 * 차종 (car_type)
	 */
	    private String cartype;         // car_type - 차종
	/**
	 * 연료 타입 (car_fuel_type)
	 */
	    private String carfueltype;     // car_fuel_type - 연료타입
	/**
	 * 차량 모델 (car_name)
	 */
	    private String carname;         // car_name - 차량모델
}
