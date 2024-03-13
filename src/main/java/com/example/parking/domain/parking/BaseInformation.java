package com.example.parking.domain.parking;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class BaseInformation {

    private String name;
    private String tel;
    private String address;

    @Embedded
    private PayTypes payTypes;

    @Enumerated(STRING)
    private ParkingType parkingType;

    @Enumerated(STRING)
    private OperationType operationType;

    public BaseInformation(String name, String tel, String address, PayTypes payTypes, ParkingType parkingType,
                           OperationType operationType) {
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.payTypes = payTypes;
        this.parkingType = parkingType;
        this.operationType = operationType;
    }

    public boolean containsOperationType(List<OperationType> operationTypes) {
        return operationTypes.stream()
                .anyMatch(operationType -> this.operationType == operationType);
    }

    public boolean containsParkingType(List<ParkingType> parkingTypes) {
        return parkingTypes.stream()
                .anyMatch(parkingType -> this.parkingType == parkingType);
    }

    public boolean containsPayType(List<PayType> memberPayTypes) {
        return this.payTypes.contains(memberPayTypes);
    }
}
