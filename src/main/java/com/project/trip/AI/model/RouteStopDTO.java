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
	private String restaurantCategory;
	private String transportationMode;
	
	//헬스케어
	private Double walkingDistanceKm;
	private Integer walkingStepsCount;

}
