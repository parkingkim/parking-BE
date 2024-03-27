package com.example.parking.application.parking;

import com.example.parking.application.parking.dto.ParkingDetailInfoResponse;
import com.example.parking.application.parking.dto.ParkingDetailInfoResponse.FeeInfo;
import com.example.parking.application.review.ReviewService;
import com.example.parking.application.review.dto.ReviewInfoResponse;
import com.example.parking.domain.parking.Parking;
import com.example.parking.domain.parking.ParkingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final ReviewService reviewService;

    @Transactional
    public void saveAll(List<Parking> parkingLots) {
        parkingRepository.saveAll(parkingLots);
    }

    @Transactional(readOnly = true)
    public Set<Parking> getParkingLots(Set<String> parkingNames) {
        return parkingRepository.findAllByBaseInformationNameIn(parkingNames);
    }

    @Transactional(readOnly = true)
    public ParkingDetailInfoResponse findParking(Long parkingId, LocalDateTime now) {
        ReviewInfoResponse reviews = reviewService.readReviews(parkingId);
        Parking parking = parkingRepository.getById(parkingId);
        int diffMinute = parking.calculateUpdatedDiff(now);

        return toParkingResponse(reviews, parking, diffMinute);
    }

    private ParkingDetailInfoResponse toParkingResponse(ReviewInfoResponse reviews, Parking parking, int diffMinute) {
        return new ParkingDetailInfoResponse(
                parking.getBaseInformation().getName(),
                parking.getBaseInformation().getParkingType().getDescription(),
                parking.getLocation().getLatitude(),
                parking.getLocation().getLongitude(),
                new FeeInfo(
                        parking.getFeePolicy().getBaseFee().getFee(),
                        parking.getFeePolicy().getBaseTimeUnit().getTimeUnit()
                ),
                parking.getSpace().getCurrentParking(),
                parking.getSpace().getCapacity(),
                diffMinute,
                parking.getBaseInformation().getTel(),
                parking.getBaseInformation().getPayTypes().getDescription(),
                reviews
        );
    }
}
