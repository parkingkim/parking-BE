package com.example.parking.application.parking;

import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.parking.application.parking.dto.ParkingDetailInfoResponse;
import com.example.parking.application.review.dto.ReviewCreateRequest;
import com.example.parking.container.ContainerTest;
import com.example.parking.domain.member.Member;
import com.example.parking.domain.member.Password;
import com.example.parking.domain.parking.BaseInformation;
import com.example.parking.domain.parking.Fee;
import com.example.parking.domain.parking.FeePolicy;
import com.example.parking.domain.parking.FreeOperatingTime;
import com.example.parking.domain.parking.Location;
import com.example.parking.domain.parking.OperatingTime;
import com.example.parking.domain.parking.OperationType;
import com.example.parking.domain.parking.Parking;
import com.example.parking.domain.parking.ParkingType;
import com.example.parking.domain.parking.PayTypes;
import com.example.parking.domain.parking.Space;
import com.example.parking.domain.parking.TimeInfo;
import com.example.parking.domain.parking.TimeUnit;
import com.example.parking.domain.review.Content;
import com.example.parking.support.exception.DomainException;
import com.example.parking.support.exception.ExceptionInformation;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ParkingServiceTest extends ContainerTest {

    @Test
    void 조회하려는_주차장이_없으면_예외() {
        //given
        Assertions.assertThatThrownBy(() -> parkingService.findParking(1L, LocalDateTime.now()))
                .isInstanceOf(DomainException.class)
                .hasMessage(ExceptionInformation.INVALID_PARKING.getMessage());
    }

    @Test
    void 주차장_상세조회() {
        //given
        String parkingName = "호이주차장";
        Parking parking = makeParking(parkingName);
        List<Parking> parkings = List.of(parking);
        parkingService.saveAll(parkings);

        Member member = new Member("하디", "email", "동혁", new Password("qwer1234"));
        memberRepository.save(member);

        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                List.of(Content.LARGE_PARKING_SPACE.getDescription(), Content.EASY_TO_PAY.getDescription()));
        reviewService.createReview(parking.getId(), member.getId(), reviewCreateRequest);

        // when, then
        ParkingDetailInfoResponse parkingDetailInfoResponse = parkingService.findParking(parking.getId(),
                LocalDateTime.now());
        assertAll(
                () -> Assertions.assertThat(parkingDetailInfoResponse.getReviewInfo().reviews()).hasSize(2),
                () -> Assertions.assertThat(parkingDetailInfoResponse.getParkingName()).isEqualTo(parkingName)
        );
    }

    private Parking makeParking(String parkingName) {
        return new Parking
                (
                        new BaseInformation(parkingName, "010", "부산", PayTypes.DEFAULT, ParkingType.MECHANICAL,
                                OperationType.PRIVATE),
                        Location.of(100d, 100d),
                        Space.of(100, 30),
                        new FreeOperatingTime(
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30)),
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30)),
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30))
                        ),
                        new OperatingTime(
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30)),
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30)),
                                new TimeInfo(LocalTime.of(10, 30), LocalTime.of(20, 30))
                        ),
                        new FeePolicy(
                                Fee.from(2000),
                                Fee.from(2000),
                                TimeUnit.from(30),
                                TimeUnit.from(15),
                                Fee.from(50000))
                );
    }

}
