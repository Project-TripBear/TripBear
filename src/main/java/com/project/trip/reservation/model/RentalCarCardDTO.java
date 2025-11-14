package com.project.trip.reservation.model;

import lombok.Data;

@Data
public class RentalCarCardDTO {
	private Long carId;
    private Long placeLocationId;
    private String carName;
    private String carType;
    private String carNumber;
    private String fuelType;
    private int seats;
    private int pricePerDay;
    private String carImageUrl;
}
