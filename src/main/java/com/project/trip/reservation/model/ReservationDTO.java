package com.project.trip.reservation.model;

import lombok.Data;

/**
 * 통합 예약의 기본 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblReservation` 테이블과 매핑됩니다.
 */
@Data
public class ReservationDTO {

    /**
     * 예약 고유 번호 (PK)
     */
    private long reservationId;         // 예약 PK
    /**
     * 회원 ID
     */
    private long userId;                // 회원 ID
    /**
     * AI 추천 루트 ID (없을 수도 있음)
     */
    private long userRouteId;           // AI 추천 루트 ID (없을 수도 있으니 long)
    /**
     * 예약 상태 (1:예약, 2:취소 등)
     */
    private Integer statusId = 1;       // 예약 상태 (1:예약,2:취소 등)
    /**
     * 총 가격 (렌터카 + 숙소)
     */
    private long reservationPrice;      // 총 가격 (car + accom)
    /**
     * 예약 시작일
     */
    private String reservationStartDate;
    /**
     * 예약 종료일
     */
    private String reservationEndDate;
    /**
     * 예약 등록일
     */
    private String reservationRegdate;

    /**
     * 숙소 예약 고유 번호 (조인용)
     */
    private Long accomReservationId;
    /**
     * 렌터카 예약 고유 번호 (조인용)
     */
    private Long carReservationId;

    /**
     * 숙소 이름 (조인용)
     */
    private String accomName;
    /**
     * 객실 이름 (조인용)
     */
    private String roomName;
    /**
     * 차량 이름 (조인용)
     */
    private String carName;
    /**
     * 예약 지역 (조인용)
     */
    private String region;
    
}
