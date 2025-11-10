package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationStatusDTO {

    private Integer statusId;   // 상태 번호(PK)
    private String statusName;  // 상태 이름 (예: 예약완료 / 이용완료 / 취소 등)
    private String statusDesc;  // 상태 설명 (관리자용 문구)

}

