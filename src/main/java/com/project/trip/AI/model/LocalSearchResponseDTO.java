package com.project.trip.AI.model;

public class LocalSearchResponseDTO {

    private double lat;
    private double lng;
    private String name;

    public LocalSearchResponseDTO(double lat, double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public String getName() { return name; }

    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
    public void setName(String name) { this.name = name; }
}
