package com.example.parking.domain.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LocationTest {

    @Test
    void 위도와_경도를_통해_Point객체를_가진_Location객체를_생성한다() {
        double latitude = 37.4809626;
        double longitude = 127.1216765;

        Location location = Location.of(longitude, latitude);

        assertAll(
                () -> assertThat(location.getLongitude()).isEqualTo(longitude),
                () -> assertThat(location.getLatitude()).isEqualTo(latitude)
        );
    }

    @CsvSource({"0.1, -", " , 0.2", "nil, nil"})
    @ParameterizedTest
    void 이상한_값이_들어오면_특정_음수_좌표를_반환한다(String longitude, String latitude) {
        //given, when
        Location actual = Location.of(longitude, latitude);
        Location expected = Location.of("-1.0", "-1.0");

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
