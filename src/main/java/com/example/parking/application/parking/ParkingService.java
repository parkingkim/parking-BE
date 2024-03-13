package com.example.parking.application.parking;

import com.example.parking.application.parking.dto.ParkingLotsResponse;
import com.example.parking.application.parking.dto.ParkingLotsResponse.ParkingResponse;
import com.example.parking.application.parking.dto.ParkingQueryRequest;
import com.example.parking.domain.favorite.Favorite;
import com.example.parking.domain.favorite.FavoriteRepository;
import com.example.parking.domain.parking.Location;
import com.example.parking.domain.parking.Parking;
import com.example.parking.domain.parking.repository.ParkingRepository;
import com.example.parking.domain.searchcondition.SearchCondition;
import com.example.parking.domain.searchcondition.SearchConditionRepository;
import com.example.parking.support.Association;
import com.example.parking.support.exception.ClientException;
import com.example.parking.support.exception.ExceptionInformation;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ParkingService {

    public static final String DISTANCE_ORDER_CONDITION = "가까운 순";
    private final ParkingRepository parkingRepository;
    private final ParkingDomainService parkingDomainService;
    private final SearchConditionRepository searchConditionRepository;
    private final FavoriteRepository favoriteRepository;

    public ParkingLotsResponse findParkingLots(ParkingQueryRequest parkingQueryRequest, Long memberId) {
        Location destination = Location.of(parkingQueryRequest.getLongitude(), parkingQueryRequest.getLatitude());
        SearchCondition searchCondition = findMemberSearchCondition(memberId);
        List<Favorite> favorites = favoriteRepository.findByMemberId(Association.from(memberId));
        List<Parking> parkingLots = findParkingLotsByOrderCondition(parkingQueryRequest, destination);
        List<Parking> filteredParkingLots = filteringByCondition(parkingLots, memberId);

        List<ParkingResponse> parkingResponses = parkingDomainService.calculateParkingInfo(filteredParkingLots,
                searchCondition, destination, favorites);

        return new ParkingLotsResponse(parkingResponses);
    }

    private SearchCondition findMemberSearchCondition(Long memberId) {
        return searchConditionRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ClientException(ExceptionInformation.INVALID_SEARCH_CONDITION));
    }

    private List<Parking> filteringByCondition(List<Parking> parkingLots, Long memberId) {
        SearchCondition memberSearchCondition = findMemberSearchCondition(memberId);
        return parkingDomainService.filterByCondition(parkingLots, memberSearchCondition);
    }

    private List<Parking> findParkingLotsByOrderCondition(ParkingQueryRequest parkingQueryRequest,
                                                          Location middleLocation) {
        if (parkingQueryRequest.getPriority().equals(DISTANCE_ORDER_CONDITION)) {
            return parkingRepository.findAroundParkingLotsOrderByDistance(middleLocation.getPoint(),
                    parkingQueryRequest.getRadius());
        }
        return parkingRepository.findAroundParkingLots(middleLocation.getPoint(), parkingQueryRequest.getRadius());
    }

    @Transactional
    public void saveAll(List<Parking> parkingLots) {
        parkingRepository.saveAll(parkingLots);
    }

    @Transactional(readOnly = true)
    public Set<Parking> getParkingLots(Set<String> parkingNames) {
        return parkingRepository.findAllByBaseInformationNameIn(parkingNames);
    }
}
