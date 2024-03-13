package com.example.parking.domain.parking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ParkingFeeCalculator {

    public Fee calculateParkingFee(Parking parking, LocalDateTime beginTime, LocalDateTime endTime) {
        if (!parking.supportCalculateParkingFee()) {
            return Fee.NO_INFO;
        }

        List<DayParking> dayParkingDates = separateDate(beginTime, endTime);
        List<Integer> payOfChargeMinutesPerDay = dayParkingDates.stream()
                .map(parking::calculatePayOfChargeMinutes)
                .toList();

        return payOfChargeMinutesPerDay.stream()
                .filter(minutes -> minutes > 0)
                .map(parking::calculateParkingFee)
                .reduce(Fee::plus)
                .orElse(Fee.ZERO);
    }

    private List<DayParking> separateDate(LocalDateTime beginTime, LocalDateTime endTime) {
        if (isSameDate(beginTime, endTime)) {
            return List.of(
                    new DayParking(Day.from(beginTime.getDayOfWeek()), beginTime.toLocalTime(), endTime.toLocalTime()));
        }

        List<DayParking> dayParkingDates = new ArrayList<>();
        dayParkingDates.add(makeFirstDayParking(beginTime));
        beginTime = beginTime.plusDays(1);
        while (!isSameDate(beginTime, endTime)) {
            dayParkingDates.add(new DayParking(Day.from(beginTime.getDayOfWeek()), LocalTime.MIN, LocalTime.MAX));
            beginTime = beginTime.plusDays(1);
        }
        dayParkingDates.add(makeLastDayParking(endTime));
        return dayParkingDates;
    }

    private boolean isSameDate(LocalDateTime beginTime, LocalDateTime endTime) {
        return isSameYear(beginTime, endTime) &&
                isSameMonth(beginTime, endTime) &&
                isSameDayOfMonth(beginTime, endTime);
    }

    private boolean isSameYear(LocalDateTime beginTime, LocalDateTime endTime) {
        return beginTime.getYear() == endTime.getYear();
    }

    private boolean isSameMonth(LocalDateTime beginTime, LocalDateTime endTime) {
        return beginTime.getMonth().equals(endTime.getMonth());
    }

    private boolean isSameDayOfMonth(LocalDateTime beginTime, LocalDateTime endTime) {
        return beginTime.getDayOfMonth() == endTime.getDayOfMonth();
    }

    private DayParking makeFirstDayParking(LocalDateTime beginTime) {
        return new DayParking(Day.from(beginTime.getDayOfWeek()), beginTime.toLocalTime(), LocalTime.MAX);
    }

    private DayParking makeLastDayParking(LocalDateTime endTime) {
        return new DayParking(Day.from(endTime.getDayOfWeek()), LocalTime.MIN, endTime.toLocalTime());
    }
}
