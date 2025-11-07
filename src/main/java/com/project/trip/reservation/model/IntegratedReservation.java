package com.project.trip.reservation.model;

import lombok.Data;

@Data
public class IntegratedReservation {

    private ReservationDTO reservation;          // 통합예약 (PK)
    private AccomReservationDTO accomReservation; // ✅ 숙소예약 필수 (null 불가)
    private CarReservationDTO carReservation;     // ✅ 차량예약 선택 (null 가능)
}
