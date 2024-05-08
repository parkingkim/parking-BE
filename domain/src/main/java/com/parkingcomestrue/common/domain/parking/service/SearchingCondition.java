package com.parkingcomestrue.common.domain.parking.service;

import com.parkingcomestrue.common.domain.parking.OperationType;
import com.parkingcomestrue.common.domain.parking.ParkingType;
import com.parkingcomestrue.common.domain.parking.PayType;
import com.parkingcomestrue.common.domain.searchcondition.FeeType;
import java.util.List;
import lombok.Getter;

@Getter
public class SearchingCondition {

    private final List<OperationType> operationTypes;
    private final List<ParkingType> parkingTypes;
    private final List<PayType> payTypes;
    private final FeeType feeType;
    private final Integer hours;

    public SearchingCondition(List<OperationType> operationTypes, List<ParkingType> parkingTypes,
                              List<PayType> payTypes,
                              FeeType feeType, Integer hours) {
        this.operationTypes = operationTypes;
        this.parkingTypes = parkingTypes;
        this.payTypes = payTypes;
        this.feeType = feeType;
        this.hours = hours;
    }
}