package com.project.trip.allplace.model;

import java.util.List;

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
    private String placeApiId;
    private long placeTypeId;
    private long placeLocationId;

    private String name;
    private String address;
    private double latitude;     
    private double longitude;
    private String placeMainImageUrl;
    private double distance;
    
    private List<String> hashtags;
    
    private TouristSpotDTO touristSpotDetail;
    private EventDTO eventDetail;
    private RestaurantDTO restaurantDetail;
    private String contentTypeId;
}