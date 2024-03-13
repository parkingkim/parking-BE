package com.example.parking.fake;

import com.example.parking.application.parking.ParkingDomainService;
import com.example.parking.application.parking.ParkingService;
import com.example.parking.domain.parking.ParkingFeeCalculator;

public class FakeParkingService extends ParkingService {

    private final BasicParkingRepository repository;

    public FakeParkingService(BasicParkingRepository repository) {
        super(repository, new ParkingDomainService(new ParkingFeeCalculator()), new FakeSearchConditionRepository(),
                new FakeFavoriteRepository());
        this.repository = repository;
    }

    public int count() {
        return repository.count();
    }
}
