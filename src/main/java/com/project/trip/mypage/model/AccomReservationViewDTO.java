package com.project.trip.mypage.model;

import lombok.Data;

@Data
public class AccomReservationViewDTO {
    private String seq;             // reservation_id - 예약번호
    private String useq;            // user_id - 사용자ID
    private String roomtotalprice;  // room_total_price - 총 금액
    private String accomseq;        // accom_reservation_id - 숙소 예약ID
    private String roomname;        // room_name - 객실명
    private String placename;       // place_name - 숙소명
    private String placeaddress;    // place_address - 주소
    private String roomtype;        // room_type - 객실 타입
    private String accomtype;       // accom_type - 숙소 타입
    private String guestcount;      // guest_count - 인원 수
    private String checkindate;     // checkin_date - 체크인
    private String checkoutdate;    // checkout_date - 체크아웃
}
