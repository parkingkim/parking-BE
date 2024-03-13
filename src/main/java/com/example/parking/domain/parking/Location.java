package com.example.parking.domain.parking;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final Location NO_PROVIDE = Location.of(-1.0, -1.0);

    private Point point;

    private Location(Point point) {
        this.point = point;
    }

    public static Location of(Double longitude, Double latitude) {
        try {
            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            return new Location(point);
        } catch (NullPointerException e) {
            return NO_PROVIDE;
        }
    }

    public static Location of(String longitude, String latitude) {
        try {
            return Location.of(Double.parseDouble(longitude), Double.parseDouble(latitude));
        } catch (NumberFormatException | NullPointerException e) {
            return NO_PROVIDE;
        }
    }

    public double getLatitude() {
        return point.getY();
    }

    public double getLongitude() {
        return point.getX();
    }
}
