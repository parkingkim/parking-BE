package com.parkingcomestrue.fake;

import com.parkingcomestrue.external.parkingapi.ParkingApiService;
import com.parkingcomestrue.parking.domain.parking.BaseInformation;
import com.parkingcomestrue.parking.domain.parking.Fee;
import com.parkingcomestrue.parking.domain.parking.FeePolicy;
import com.parkingcomestrue.parking.domain.parking.FreeOperatingTime;
import com.parkingcomestrue.parking.domain.parking.Location;
import com.parkingcomestrue.parking.domain.parking.OperatingTime;
import com.parkingcomestrue.parking.domain.parking.OperationType;
import com.parkingcomestrue.parking.domain.parking.Parking;
import com.parkingcomestrue.parking.domain.parking.ParkingType;
import com.parkingcomestrue.parking.domain.parking.PayTypes;
import com.parkingcomestrue.parking.domain.parking.Space;
import com.parkingcomestrue.parking.domain.parking.TimeUnit;
import java.util.LinkedList;
import java.util.List;

public class NotOfferCurrentParkingApiService implements ParkingApiService {

    private int readSize;

    public NotOfferCurrentParkingApiService(int readSize) {
        this.readSize = readSize;
    }

    @Override
    public boolean offerCurrentParking() {
        return false;
    }

    @Override
    public List<Parking> read() {
        LinkedList<Parking> result = new LinkedList<>();
        for (int i = 0; i < readSize; i++) {
            Parking parking = new Parking(
                    new BaseInformation("not offer parking" + i, "051-000" + i, "부산시 어딘가 " + i, PayTypes.DEFAULT,
                            ParkingType.NO_INFO,
                            OperationType.PUBLIC),
                    Location.of("33.333" + i, "44.444" + i),
                    Space.of(-1, -1),
                    FreeOperatingTime.ALWAYS_FREE,
                    OperatingTime.ALWAYS_OPEN,
                    new FeePolicy(Fee.ZERO, Fee.ZERO, TimeUnit.from(0), TimeUnit.from(0), Fee.ZERO)
            );
            result.add(parking);
        }

        return result;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }
}
