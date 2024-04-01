package com.example.parking.application.parking;

import com.example.parking.domain.parking.Parking;
import com.example.parking.domain.parking.ParkingFeeCalculator;
import com.example.parking.domain.parking.SearchingCondition;
import com.example.parking.domain.searchcondition.FeeType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ParkingApplicationService {

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
