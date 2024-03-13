package com.example.parking.api.parking;

import com.example.parking.application.parking.ParkingService;
import com.example.parking.application.parking.dto.ParkingLotsResponse;
import com.example.parking.application.parking.dto.ParkingQueryRequest;
import com.example.parking.config.argumentresolver.MemberAuth;
import com.example.parking.config.argumentresolver.parking.ParkingQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParkingController {

    private final ParkingService parkingService;

    @GetMapping("/parkings")
    public ResponseEntity<ParkingLotsResponse> find(@ParkingQuery ParkingQueryRequest parkingQueryRequest,
                                                    @MemberAuth Long memberId) {
        ParkingLotsResponse parkingLots = parkingService.findParkingLots(parkingQueryRequest, memberId);
        return ResponseEntity.ok(parkingLots);
    }
}
