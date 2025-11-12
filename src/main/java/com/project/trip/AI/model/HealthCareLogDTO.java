package com.project.trip.AI.model;

import lombok.Data;

@Data
public class HealthCareLogDTO {

	private long healthcareId;
	private long userId;
	private long aiRouteStopId;
	private String healthcareDate;
	private Double healthcareDistanceKm;
	private Integer healthcareStepsCount;
	private Double healthcareCaloriesBurned;
}
