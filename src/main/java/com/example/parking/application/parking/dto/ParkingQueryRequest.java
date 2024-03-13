package com.example.parking.application.parking.dto;

import lombok.Getter;

@Getter
public class ParkingQueryRequest {

    private final String priority;
    private final Double latitude;
    private final Double longitude;
    private final Integer radius;

    public ParkingQueryRequest(String priority, Double latitude, Double longitude, Integer radius) {
        this.priority = priority;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}
