package com.project.trip.allplace.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlaceDTO {

    private long placeId;        
    private String apiContentId; 
    private String name;
    private String address;
    private double latitude;     
    private double longitude;
    // ... (기타 필요한 필드, 예: firstImage)
}