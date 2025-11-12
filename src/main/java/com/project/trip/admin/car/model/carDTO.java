package com.project.trip.admin.car.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class carDTO {
    private int carId;
    private String carName;
    private String carType;
    private String fuelType;
    private int pricePerDay;
    private boolean isReserved; // 예약 여부
}
