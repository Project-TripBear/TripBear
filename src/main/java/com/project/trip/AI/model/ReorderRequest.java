package com.project.trip.AI.model;

import java.util.List;

public class ReorderRequest {

    private int day;
    private List<StopOrderDTO> stops;

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public List<StopOrderDTO> getStops() {
        return stops;
    }
    public void setStops(List<StopOrderDTO> stops) {
        this.stops = stops;
    }
}
