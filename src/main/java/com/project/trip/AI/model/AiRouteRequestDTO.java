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
	
	private String startDate;
	private String endDate;
	
<<<<<<< HEAD
	
=======
	private PhysicalInfo physicalInfo;
	
	private String healthGoal;
	private String foodPreference;
	private String healthCondition;
	
	@Data
>>>>>>> 691aab35894de916ac1f2d9835cba4b568572f74
	public static class PhysicalInfo {
		private String gender;
		private String height;
		private String weight;
	}
<<<<<<< HEAD

=======
	
>>>>>>> 691aab35894de916ac1f2d9835cba4b568572f74
}