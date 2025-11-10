package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccomReservationDTO {

    private Long accomReservationId;   // 객실예약고유번호(PK)
    private Long roomId;               // 객실고유번호(FK)
    private Integer statusId;          // 예약상태번호(FK)
    private Long userRouteId;          // 사용자저장루트번호(FK)
    private Integer guestCount;        // 투숙 인원 수
    private Integer roomTotalPrice;    // 총 금액
    private String checkinDate;        // 체크인 날짜
    private String checkoutDate;       // 체크아웃 날짜
    private String accomNotes;         // 요청사항
    private Long userId;               // 회원번호(FK)
    
    private Long reservationId;

}
