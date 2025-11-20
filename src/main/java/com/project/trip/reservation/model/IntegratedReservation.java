package com.project.trip.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 통합 예약 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 하나의 예약에 숙소 예약과 렌터카 예약 정보를 함께 묶어서 처리할 때 사용됩니다.
 * 예약 관련 DTO들과 화면 표시를 위한 추가 파라미터들을 포함합니다.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegratedReservation {

    /**
     * 통합 예약의 기본 정보 (PK)
     */
    private ReservationDTO reservation;          // 통합예약 (PK)
    /**
     * 숙소 예약 정보 (필수)
     */
    private AccomReservationDTO accomReservation; // ✅ 숙소예약 필수 (null 불가)
    /**
     * 렌터카 예약 정보 (선택 사항)
     */
    private CarReservationDTO carReservation;     // ✅ 차량예약 선택 (null 가능)

    // ✅ 추가적으로 화면용 파라미터들
    /**
     * 예약 지역
     */
    private String region;
    /**
     * 체크인 날짜
     */
    private String checkin;
    /**
     * 체크아웃 날짜
     */
    private String checkout;
    /**
     * 객실 고유 번호
     */
    private Long roomId;
    /**
     * 렌터카 고유 번호
     */
    private Long carId;
    
    /**
     * 숙소 카드 정보
     */
    private AccomRoomCardDTO room;         // 숙소 카드 정보
    /**
     * 렌터카 카드 정보
     */
    private RentalCarCardDTO car;          // 차량 카드 정보

    /**
     * 통합 예약 정보를 생성하는 생성자입니다.
     *
     * @param reservation 통합 예약 기본 정보
     * @param accomReservation 숙소 예약 정보
     * @param carReservation 렌터카 예약 정보
     */
    public IntegratedReservation(ReservationDTO reservation, AccomReservationDTO accomReservation, CarReservationDTO carReservation) {
        this.reservation = reservation;
        this.accomReservation = accomReservation;
        this.carReservation = carReservation;
    }

}


