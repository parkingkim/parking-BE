package com.example.parking.domain.parking.dto;

import com.example.parking.domain.parking.OperationType;
import com.example.parking.domain.parking.ParkingType;
import com.example.parking.domain.parking.PayTypes;
import lombok.Getter;

@Getter
public class ParkingQueryCondition {

    private OperationType operationType;
    private ParkingType parkingType;
    private Boolean cardEnabled;
    private PayTypes payTypes;

    public ParkingQueryCondition(OperationType operationType, ParkingType parkingType, Boolean cardEnabled,
                                 PayTypes payTypes) {
        this.operationType = operationType;
        this.parkingType = parkingType;
        this.cardEnabled = cardEnabled;
        this.payTypes = payTypes;
    }
}
