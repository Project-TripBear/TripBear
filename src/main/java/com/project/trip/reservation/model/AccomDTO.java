package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccomDTO {

    private Long accomId;        // 숙소고유번호(PK)
    private Long placeId;        // 장소번호(FK)
    private String accomType;    // 호텔, 모텔, 펜션 등
    private String accomTel;     // 숙소전화번호
    private String accomRegdate; // 등록일(기본 SYSDATE)
}

