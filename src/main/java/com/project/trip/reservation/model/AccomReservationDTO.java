package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 숙소 예약 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblAccomReservation` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
public class AccomReservationDTO {

    /**
     * 객실 예약 고유 번호 (PK)
     */
    private Long accomReservationId;   // 객실예약고유번호(PK)
    /**
     * 객실 고유 번호 (FK)
     */
    private Long roomId;               // 객실고유번호(FK)
    /**
     * 예약 상태 번호 (FK)
     */
    private Integer statusId;          // 예약상태번호(FK)
    /**
     * 사용자 저장 루트 번호 (FK)
     */
    private Long userRouteId;          // 사용자저장루트번호(FK)
    /**
     * 투숙 인원 수
     */
    private Integer guestCount;        // 투숙 인원 수
    /**
     * 총 금액
     */
    private Integer roomTotalPrice;    // 총 금액
    /**
     * 체크인 날짜
     */
    private String checkinDate;        // 체크인 날짜
    /**
     * 체크아웃 날짜
     */
    private String checkoutDate;       // 체크아웃 날짜
    /**
     * 요청사항
     */
    private String accomNotes;         // 요청사항
    /**
     * 회원 번호 (FK)
     */
    private Long userId;               // 회원번호(FK)
    
    /**
     * 예약 고유 번호 (FK)
     */
    private Long reservationId;
    /**
     * 숙소 이름
     */
    private String accomName;
    /**
     * 객실 이름
     */
    private String roomName;



}
