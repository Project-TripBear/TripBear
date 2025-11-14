package com.project.trip.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegratedReservation {

    private ReservationDTO reservation;          // 통합예약 (PK)
    private AccomReservationDTO accomReservation; // ✅ 숙소예약 필수 (null 불가)
    private CarReservationDTO carReservation;     // ✅ 차량예약 선택 (null 가능)

    // ✅ 추가적으로 화면용 파라미터들
    private String region;
    private String checkin;
    private String checkout;
    private String people;
    private Long roomId;
    private Long carId;
    
    private AccomRoomCardDTO room;         // 숙소 카드 정보
    private RentalCarCardDTO car;          // 차량 카드 정보

    public IntegratedReservation(ReservationDTO reservation, AccomReservationDTO accomReservation, CarReservationDTO carReservation) {
        this.reservation = reservation;
        this.accomReservation = accomReservation;
        this.carReservation = carReservation;
    }

}


