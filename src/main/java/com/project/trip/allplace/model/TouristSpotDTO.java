package com.project.trip.allplace.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TouristSpotDTO {

	private long placeId;
    private String spotOverinfo;
    private String admissionFee;
    private String openingHours;
    private String contactInfo;
    private String parkingInfo;
    private String restDay;
}