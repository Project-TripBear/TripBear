package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccomRoomDTO {

    private Long roomId;         // 객실고유번호(PK)
    private Long accomId;        // 숙소고유번호(FK)
    private String roomName;     // 객실명
    private String roomType;     // 스탠다드, 스위트 등
    private Integer capacity;    // 수용인원
    private Integer pricePerNight; // 1박 요금
    private String roomArea;     // 객실 크기
    private String roomImageUrl; // 대표 이미지
    private String roomStatus;   // y/n 사용가능 여부
    private String roomRegdate;  // 등록일
}

