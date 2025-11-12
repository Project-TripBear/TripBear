package com.project.trip.AI.model;

import lombok.Data;

@Data
public class AiRouteRequestDTO {
	
	private String city;
	private String duration;
	private String travelStyle;
	private String activityTime;
	private String budget;
	private String preferredArea;
	private String transportation;
	private String activityType;
	private String companion;
	
	private PhysicalInfo physicalInfo;
	private String startDate;
	private String endDate;
	
	@Data
	public static class PhysicalInfo {
		private String gender;
		private double height;
		private double weight;
	}
	
public class TripPreferencesDTO {

}
}