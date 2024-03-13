package com.example.parking.external.coordinate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CoordinateResponse {

    @JsonProperty("documents")
    private List<ExactLocation> exactLocations;

    @JsonProperty("meta")
    private Meta meta;

    public CoordinateResponse(List<ExactLocation> exactLocations, Meta meta) {
        this.exactLocations = exactLocations;
        this.meta = meta;
    }

    @NoArgsConstructor
    @Getter
    public static class ExactLocation {

        @JsonProperty("x")
        private Double latitude;

        @JsonProperty("y")
        private Double longitude;
    }

    @Getter
    @NoArgsConstructor
    public static class Meta {

        @JsonProperty("total_count")
        private Integer totalCount;

        public Meta(Integer totalCount) {
            this.totalCount = totalCount;
        }
    }
}
