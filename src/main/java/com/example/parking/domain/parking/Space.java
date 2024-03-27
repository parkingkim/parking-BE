package com.example.parking.domain.parking;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space {

    private static final Integer NO_PROVIDE = -1;

    private Integer capacity;
    private Integer currentParking;

    private Space(Integer capacity, Integer currentParking) {
        this.capacity = capacity;
        this.currentParking = currentParking;
    }

    public static Space of(String capacity, String currentParking) {
        return new Space(parseInt(capacity), parseInt(currentParking));
    }

    private static Integer parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return NO_PROVIDE;
        }
    }

    public static Space of(Integer capacity, Integer currentParking) {
        if (capacity < 0) {
            capacity = NO_PROVIDE;
        }
        if (currentParking < 0) {
            currentParking = NO_PROVIDE;
        }
        return new Space(capacity, currentParking);
    }
}
