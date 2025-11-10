package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarReservationDTO {

    private Long carReservationId; // 차량예약고유번호(PK)
    private Long carId;            // 차량고유번호(FK)
    private Integer statusId;      // 예약상태번호(FK)
    private Long userRouteId;      // 사용자저장루트번호(FK)
    private String pickupDate;     // 차량픽업일
    private String dropoffDate;    // 차량반납일
    private String pickupLocation; // 차량픽업장소
    private String dropoffLocation;// 차량반납장소
    private Integer carTotalPrice; // 총 금액
    private String carNotes;       // 요청사항
    private Long userId;           // 회원번호(FK)
    
    private Long reservationId;

}
