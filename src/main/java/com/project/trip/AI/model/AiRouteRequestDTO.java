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

    private PhysicalInfo physicalInfo;

    private String healthGoal;
    private String foodPreference;
    private String healthCondition;

    @Data
    public static class PhysicalInfo {
        private String gender;
        private String height;
        private String weight;
    }

}