package com.project.trip.reservation.model;

import lombok.Data;

@Data
public class ReservationDTO {

    private long reservationId;         // 예약 PK
    private long userId;                // 회원 ID
    private long userRouteId;           // AI 추천 루트 ID (없을 수도 있으니 long)
    private int statusId;               // 예약 상태 (1:예약,2:취소 등)
    private long reservationPrice;      // 총 가격 (car + accom)
    private String reservationStartDate;
    private String reservationEndDate;
    private String reservationRegdate;

    private Long accomReservationId;
    private Long carReservationId;

    private String accomName;
    private String roomName;
    private String carName;
    private String region;
}
