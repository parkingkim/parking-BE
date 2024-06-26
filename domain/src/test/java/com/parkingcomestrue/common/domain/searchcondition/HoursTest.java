package com.parkingcomestrue.common.domain.searchcondition;

import static com.parkingcomestrue.common.support.exception.DomainExceptionInformation.INVALID_HOURS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.parkingcomestrue.common.domain.searchcondition.Hours;
import com.parkingcomestrue.common.support.exception.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HoursTest {

    @CsvSource({"1, false", "12, false", "24, false", "0, true", "13, true"})
    @ParameterizedTest
    void 이용_시간은_1시간에서_12시간_사이_또는_24시간만_선택_가능하다(int hours, boolean hasException) {
        //given, when, then
        if (hasException) {
            Assertions.assertThatThrownBy(() -> Hours.from(hours))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(INVALID_HOURS.getMessage());
            return;
        }
        assertDoesNotThrow(() -> Hours.from(hours));
    }
}
