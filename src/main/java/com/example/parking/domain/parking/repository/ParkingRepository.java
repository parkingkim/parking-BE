package com.example.parking.domain.parking.repository;

import com.example.parking.domain.parking.Parking;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface ParkingRepository extends Repository<Parking, Long> {

    default Parking getById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("익셉션 !!"));
    }

    Optional<Parking> findById(Long id);

    @Query("""
            SELECT p 
            FROM Parking p 
            WHERE ST_Contains(ST_Buffer(:point, :radius), p.location.point)
            """
    )
    List<Parking> findAroundParkingLots(@Param("point") Point point, @Param("radius") int radius);

    @Query("""
            SELECT p 
            FROM Parking p 
            WHERE ST_Contains(ST_Buffer(:point, :radius), p.location.point)
            ORDER BY ST_DISTANCE_SPHERE(:point, p.location.point)
            """
    )
    List<Parking> findAroundParkingLotsOrderByDistance(
            @Param("point") Point point,
            @Param("radius") int radius
    );

    Set<Parking> findAllByBaseInformationNameIn(Set<String> parkingNames);

    void saveAll(Iterable<Parking> parkingLots);
}
