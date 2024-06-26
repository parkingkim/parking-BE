package com.parkingcomestrue.common.domain.parking.service;

import com.parkingcomestrue.common.domain.searchcondition.FeeType;
import com.parkingcomestrue.common.domain.parking.Parking;
import com.parkingcomestrue.common.domain.parking.ParkingFeeCalculator;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ParkingFilteringService {

    private final ParkingFeeCalculator parkingFeeCalculator;

    public List<Parking> filterByCondition(List<Parking> parkingLots, SearchingCondition searchingCondition,
                                           LocalDateTime now) {
        return parkingLots.stream()
                .filter(parking -> checkFeeTypeIsPaid(searchingCondition) || checkFeeTypeAndFeeFree(searchingCondition,
                        now, parking))
                .filter(parking -> parking.containsOperationType(searchingCondition.getOperationTypes()))
                .filter(parking -> parking.containsParkingType(searchingCondition.getParkingTypes()))
                .filter(parking -> parking.containsPayType(searchingCondition.getPayTypes()))
                .toList();
    }

    private boolean checkFeeTypeIsPaid(SearchingCondition searchingCondition) {
        return searchingCondition.getFeeType() == FeeType.PAID;
    }

    private boolean checkFeeTypeAndFeeFree(SearchingCondition searchingCondition, LocalDateTime now, Parking parking) {
        return searchingCondition.getFeeType() == FeeType.FREE && parkingFeeCalculator.calculateParkingFee(parking, now,
                now.plusHours(searchingCondition.getHours())).isFree();
    }
}
