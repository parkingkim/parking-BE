package com.example.parking.domain.parking;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParkingTest {

    @Test
    void 목적지와_68미터_떨어진_주차장_도보_예상시간_계산() {
        // given (parking 과 destination 거리 68m)
        int expectedTime = (int) Math.ceil(0.068 / 5);
        Parking parking = Parking.builder()
                .location(Location.of(127.1215865, 37.4811181))
                .build();
        Location destination = Location.of(127.1213647, 37.4817298);

        // when
        int walkingTime = parking.calculateWalkingTime(destination);

        // then
        Assertions.assertThat(walkingTime).isEqualTo(expectedTime);
    }

    @Test
    void 목적지와_333미터_떨어진_주차장_도보_예상시간_계산() {
        // given (parking 과 destination 거리 68m)
        int expectedTime = (int) Math.ceil(0.333 / 5);
        Parking parking = Parking.builder()
                .location(Location.of(127.1215865, 37.4811181))
                .build();
        Location destination = Location.of(127.1186224, 37.479259);

        // when
        int walkingTime = parking.calculateWalkingTime(destination);

        // then
        Assertions.assertThat(walkingTime).isEqualTo(expectedTime);
    }

    @ParameterizedTest
    @MethodSource("getParking")
    void 주어진_주차장_정보에_따라_하루_요금_계산을_한다(Parking parking, int payOfChargeMinutes, Fee expected) {
        // given, when
        Fee actual = parking.calculateParkingFee(payOfChargeMinutes);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getParking() {
        return Stream.of(
                /*
                                기본 주차 요금 정책 : 30분당 2000원
                                추가 주차 요금 정책 : 15분당 2000원
                                하루 최대 주차 요금 : 50000원

                                주차 시간 : 100분
                                주차 금액 : 2000원 + 10000원 = 12000원
                         */
                Arguments.of(
                        new Parking
                                (
                                        new BaseInformation(),
                                        new Location(),
                                        new Space(),
                                        new FreeOperatingTime(),
                                        new OperatingTime(),
                                        new FeePolicy(
                                                Fee.from(2000),
                                                Fee.from(2000),
                                                TimeUnit.from(30),
                                                TimeUnit.from(15),
                                                Fee.from(50000))
                                ),
                        100,
                        Fee.from(12000)
                ),
                 /*
                                기본 주차 요금 정책 : 30분당 2000원
                                추가 주차 요금 정책 : 15분당 2000원
                                하루 최대 주차 요금 : 50000원

                                주차 시간 : 30분
                                주차 금액 : 2000원
                         */
                Arguments.of(
                        new Parking
                                (
                                        new BaseInformation(),
                                        new Location(),
                                        new Space(),
                                        new FreeOperatingTime(),
                                        new OperatingTime(),
                                        new FeePolicy(
                                                Fee.from(2000),
                                                Fee.from(2000),
                                                TimeUnit.from(30),
                                                TimeUnit.from(15),
                                                Fee.from(50000))
                                ),
                        30,
                        Fee.from(2000)
                ),
                /*
                                기본 주차 요금 정책 : 30분당 2000원
                                추가 주차 요금 정책 : 15분당 2000원
                                하루 최대 주차 요금 : 50000원

                                주차 시간 : 31분
                                주차 금액 : 2000 + 2000원 = 4000원
                         */
                Arguments.of(
                        new Parking
                                (
                                        new BaseInformation(),
                                        new Location(),
                                        new Space(),
                                        new FreeOperatingTime(),
                                        new OperatingTime(),
                                        new FeePolicy(
                                                Fee.from(2000),
                                                Fee.from(2000),
                                                TimeUnit.from(30),
                                                TimeUnit.from(15),
                                                Fee.from(50000))
                                ),
                        31,
                        Fee.from(4000)
                ),
                /*
                                기본 주차 요금 정책 : 30분당 2000원
                                추가 주차 요금 정책 : 15분당 2000원
                                하루 최대 주차 요금 : 50000원

                                주차 시간 : 480분
                                주차 금액 : 2000 + 60000원 = 62000원 -> 하루 최대 요금 50000원 적용
                         */
                Arguments.of(
                        new Parking
                                (
                                        new BaseInformation(),
                                        new Location(),
                                        new Space(),
                                        new FreeOperatingTime(),
                                        new OperatingTime(),
                                        new FeePolicy(
                                                Fee.from(2000),
                                                Fee.from(2000),
                                                TimeUnit.from(30),
                                                TimeUnit.from(15),
                                                Fee.from(50000))
                                ),
                        480,
                        Fee.from(50000)
                ),
                /*
                                기본 주차 요금 정책 : 30분당 2000원
                                추가 주차 요금 정책 : 15분당 2000원
                                하루 최대 주차 요금 : 50000원

                                주차 시간 : 374분
                                주차 금액 : 2000 + 46000원 = 48000원
                         */
                Arguments.of(
                        new Parking
                                (
                                        new BaseInformation(),
                                        new Location(),
                                        new Space(),
                                        new FreeOperatingTime(),
                                        new OperatingTime(),
                                        new FeePolicy(
                                                Fee.from(2000),
                                                Fee.from(2000),
                                                TimeUnit.from(30),
                                                TimeUnit.from(15),
                                                Fee.from(50000))
                                ),
                        374,
                        Fee.from(48000)
                )
        );
    }
}
