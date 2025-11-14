package com.project.trip.AI.model;

import lombok.Data;

@Data
public class RouteStopDTO {
	
	private long aiRouteStopId;
	private long aiRouteId;
	
	private int aiRouteDay;
	private int aiRouteStopOrder;
	private double aiRouteLat;
	private double aiRouteLong;
	private String aiRouteDescription;
	
	private String activityCode;
	private int durationInMinutes;
	private String transportationMode;
	

	 //헬스케어
    private double walkingDistanceKm;
    private int walkingStepsCount;
    private String restaurantCategory;


    //헬스케어 구윤추가
    private long healthcareId;
    private int healthcareCaloriesBurned;
}
