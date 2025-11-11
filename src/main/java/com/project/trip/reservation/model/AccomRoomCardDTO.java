package com.project.trip.reservation.model;

import lombok.Data;

@Data
public class AccomRoomCardDTO {
	
	private Long roomId;
    private Long accomId;
    private String accomName;
    private String roomName;
    private Long pricePerNight;
    private String address;
    private String imageUrl;
    
    private Double lat;
    private Double lng;

}
