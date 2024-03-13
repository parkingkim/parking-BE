package com.example.parking.application.parking.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ParkingLotsResponse {

    private List<ParkingResponse> parkingLots;

    private ParkingLotsResponse() {
    }

    public ParkingLotsResponse(List<ParkingResponse> parkingLots) {
        this.parkingLots = parkingLots;
    }

    @Getter
    public static class ParkingResponse {
        private Long parkingId;
        private String parkingName;
        private Integer estimatedFee;
        private Integer estimatedWalkingTime;
        private String parkingType;
        private Boolean isFavorite;
        private Double latitude;
        private Double longitude;

        private ParkingResponse() {
        }

        public ParkingResponse(Long parkingId, String parkingName, Integer estimatedFee, Integer estimatedWalkingTime,
                               String parkingType, Boolean isFavorite, Double latitude, Double longitude) {
            this.parkingId = parkingId;
            this.parkingName = parkingName;
            this.estimatedFee = estimatedFee;
            this.estimatedWalkingTime = estimatedWalkingTime;
            this.parkingType = parkingType;
            this.isFavorite = isFavorite;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
