package com.project.trip.mypage.model;

import lombok.Data;

/**
 * 마이페이지에서 숙소 예약 내역을 조회하기 위한 데이터 전송 객체(DTO)입니다.
 * 숙소 예약과 관련된 다양한 정보를 포함합니다.
 */
@Data
public class AccomReservationViewDTO {
    /**
     * 예약 번호 (reservation_id)
     */
    private String seq;             // reservation_id - 예약번호
    /**
     * 사용자 ID (user_id)
     */
    private String useq;            // user_id - 사용자ID
    /**
     * 객실 총 금액 (room_total_price)
     */
    private String roomtotalprice;  // room_total_price - 총 금액
    /**
     * 숙소 예약 ID (accom_reservation_id)
     */
    private String accomseq;        // accom_reservation_id - 숙소 예약ID
    /**
     * 객실명 (room_name)
     */
    private String roomname;        // room_name - 객실명
    /**
     * 숙소명 (place_name)
     */
    private String placename;       // place_name - 숙소명
    /**
     * 주소 (place_address)
     */
    private String placeaddress;    // place_address - 주소
    /**
     * 객실 타입 (room_type)
     */
    private String roomtype;        // room_type - 객실 타입
    /**
     * 숙소 타입 (accom_type)
     */
    private String accomtype;       // accom_type - 숙소 타입
    /**
     * 인원 수 (guest_count)
     */
    private String guestcount;      // guest_count - 인원 수
    /**
     * 체크인 날짜 (checkin_date)
     */
    private String checkindate;     // checkin_date - 체크인
    /**
     * 체크아웃 날짜 (checkout_date)
     */
    private String checkoutdate;    // checkout_date - 체크아웃
}
