package com.project.trip.reservation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RentalCarDTO {

    private Long carId;          // 차량고유번호(PK)
    private Long placeLocationId;// 지역고유번호(FK)
    private String carName;      // 차량 모델명
    private String carType;      // 소형/중형/SUV ...
    private String carNumber;    // 차량번호 UNIQUE
    private String carFuelType;  // 연료종류
    private Integer carSeats;    // 탑승 인원
    private Integer carPricePerDay; // 하루대여요금
    private String carImageUrl;  // 이미지URL
    private String carStatus;    // y/n 이용가능
    private String carRegdate;   // 등록일
}

