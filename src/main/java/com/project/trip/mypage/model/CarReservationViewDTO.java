package com.project.trip.mypage.model;

import lombok.Data;

@Data
public class CarReservationViewDTO {
	private String seq;             // reservation_id - 예약번호
	    private String useq;            // user_id - 사용자ID
	    private String carseq;          // car_reservation_id - 렌트카 예약ID
	    private String statusname;      // status_name - 상태명
	    private String pickupdate;      // pickup_date - 대여일
	    private String dropoffdate;     // dropoff_date - 반납일
	    private String pickuplocation;  // pickup_location - 대여지점
	    private String dropofflocation; // dropoff_location - 반납지점
	    private String cartotalprice;   // car_total_price - 총 금액
	    private String carnotes;        // car_notes - 비고
	    private String cartype;         // car_type - 차종
	    private String carfueltype;     // car_fuel_type - 연료타입
	    private String carname;         // car_name - 차량모델
}
