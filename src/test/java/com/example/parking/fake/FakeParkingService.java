package com.example.parking.fake;

import com.example.parking.application.parking.ParkingService;
import com.example.parking.application.review.ReviewService;

public class FakeParkingService extends ParkingService {

    private final BasicParkingRepository repository;

    public FakeParkingService(BasicParkingRepository repository, ReviewService reviewService) {
        super(repository, reviewService);
        this.repository = repository;
    }

    public int count() {
        return repository.count();
    }
}
