package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 렌터카 예약 정보를 나타내는 데이터 전송 객체(DTO)입니다.
 * `tblCarReservation` 테이블과 매핑됩니다.
 */
@Getter
@Setter
@ToString
public class CarReservationDTO {

    /**
     * 차량 예약 고유 번호 (PK)
     */
    private Long carReservationId; // 차량예약고유번호(PK)
    /**
     * 차량 고유 번호 (FK)
     */
    private Long carId;            // 차량고유번호(FK)
    /**
     * 예약 상태 번호 (FK)
     */
    private Integer statusId;      // 예약상태번호(FK)
    /**
     * 사용자 저장 루트 번호 (FK)
     */
    private Long userRouteId;      // 사용자저장루트번호(FK)
    /**
     * 차량 픽업일
     */
    private String pickupDate;     // 차량픽업일
    /**
     * 차량 반납일
     */
    private String dropoffDate;    // 차량반납일
    /**
     * 차량 픽업 장소
     */
    private String pickupLocation; // 차량픽업장소
    /**
     * 차량 반납 장소
     */
    private String dropoffLocation;// 차량반납장소
    /**
     * 총 금액
     */
    private Integer carTotalPrice; // 총 금액
    /**
     * 요청사항
     */
    private String carNotes;       // 요청사항
    /**
     * 회원 번호 (FK)
     */
    private Long userId;           // 회원번호(FK)
    
    /**
     * 예약 고유 번호 (FK)
     */
    private Long reservationId;
    /**
     * 차량 이름
     */
    private String carName;


}
