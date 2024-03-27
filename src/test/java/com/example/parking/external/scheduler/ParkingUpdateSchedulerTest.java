package com.example.parking.external.scheduler;

import com.example.parking.application.review.ReviewService;
import com.example.parking.domain.review.service.ReviewDomainService;
import com.example.parking.external.coordinate.CoordinateService;
import com.example.parking.fake.BasicParkingRepository;
import com.example.parking.fake.BasicReviewRepository;
import com.example.parking.fake.ExceptionParkingApiService;
import com.example.parking.fake.FakeCoordinateService;
import com.example.parking.fake.FakeParkingService;
import com.example.parking.fake.NotOfferCurrentParkingApiService;
import com.example.parking.fake.OfferCurrentParkingApiService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingUpdateSchedulerTest {

    private final FakeParkingService parkingService = new FakeParkingService(
            new BasicParkingRepository(),
            new ReviewService(
                    new BasicReviewRepository(),
                    new ReviewDomainService(new BasicReviewRepository())
            )
    );
    private final CoordinateService coordinateService = new FakeCoordinateService();

    @DisplayName("실시간 주차 대수를 제공하는 API에서 주차장이 0~4까지 저장되어 있는 상태에서 0~9까지 주차장을 읽어와 업데이트한다.")
    @Test
    void autoUpdateOfferCurrentParking() {
        //given
        OfferCurrentParkingApiService offerCurrentParkingApiService = new OfferCurrentParkingApiService(5);
        parkingService.saveAll(offerCurrentParkingApiService.read());
        int readSize = 10;
        offerCurrentParkingApiService.setReadSize(readSize);

        ParkingUpdateScheduler scheduler = new ParkingUpdateScheduler(
                List.of(offerCurrentParkingApiService),
                coordinateService,
                parkingService
        );

        //when
        scheduler.autoUpdateOfferCurrentParking();

        //then
        Assertions.assertThat(parkingService.count()).isEqualTo(readSize);
    }

    @DisplayName("실시간 주차 대수를 제공하지 않는 API에서 주차장이 0~4까지 저장되어 있는 상태에서 0~9까지 주차장을 읽어와 업데이트한다.")
    @Test
    void autoUpdateNotOfferCurrentParking() {
        //given
        NotOfferCurrentParkingApiService notOfferCurrentParkingApiService = new NotOfferCurrentParkingApiService(
                5);
        parkingService.saveAll(notOfferCurrentParkingApiService.read());
        int readSize = 10;
        notOfferCurrentParkingApiService.setReadSize(readSize);

        ParkingUpdateScheduler scheduler = new ParkingUpdateScheduler(
                List.of(notOfferCurrentParkingApiService),
                coordinateService,
                parkingService
        );

        //when
        scheduler.autoUpdateNotOfferCurrentParking();

        //then
        Assertions.assertThat(parkingService.count()).isEqualTo(readSize);
    }

    @DisplayName("실시간 주차 대수를 제공하는 API와 제공하지 않는 API는 영향을 안준다.")
    @Test
    void notAffectBetweenOfferAndNotOfferCurrentParking() {
        //given
        OfferCurrentParkingApiService offerCurrentParkingApiService = new OfferCurrentParkingApiService(5);
        NotOfferCurrentParkingApiService notOfferCurrentParkingApiService = new NotOfferCurrentParkingApiService(
                5);
        parkingService.saveAll(offerCurrentParkingApiService.read());
        int readSize = 10;
        notOfferCurrentParkingApiService.setReadSize(readSize);

        ParkingUpdateScheduler scheduler = new ParkingUpdateScheduler(
                List.of(offerCurrentParkingApiService, notOfferCurrentParkingApiService),
                coordinateService,
                parkingService
        );

        //when
        scheduler.autoUpdateOfferCurrentParking();

        //then
        Assertions.assertThat(parkingService.count()).isEqualTo(5);
    }

    @DisplayName("특정 API에서 예외 발생시, 해당 API는 log를 남기고 무시한다.")
    @Test
    void autoUpdateWithExceptionApi() {
        //given
        ParkingUpdateScheduler scheduler = new ParkingUpdateScheduler(
                List.of(new OfferCurrentParkingApiService(5), new ExceptionParkingApiService()),
                coordinateService,
                parkingService
        );

        //when
        scheduler.autoUpdateOfferCurrentParking();

        //then
        Assertions.assertThat(parkingService.count()).isEqualTo(5);
    }
}
