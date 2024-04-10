package com.parkingcomestrue.parking.domain.review;

import com.parkingcomestrue.parking.domain.member.Member;
import com.parkingcomestrue.parking.domain.parking.Parking;
import com.parkingcomestrue.parking.infra.converter.ContentConverter;
import com.parkingcomestrue.parking.support.Association;
import com.parkingcomestrue.parking.support.exception.DomainException;
import com.parkingcomestrue.parking.support.exception.ExceptionInformation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {

    private static final int MAX_CONTENTS_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "parking_id"))
    private Association<Parking> parkingId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "reviewer_id"))
    private Association<Member> reviewerId;

    @Convert(converter = ContentConverter.class)
    private List<Content> contents;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Review(Association<Parking> parkingId, Association<Member> reviewerId, List<Content> contents) {
        validate(contents);
        this.parkingId = parkingId;
        this.reviewerId = reviewerId;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
    }

    private static void validate(List<Content> contents) {
        if (contents == null || contents.isEmpty() || contents.size() > MAX_CONTENTS_SIZE) {
            throw new DomainException(ExceptionInformation.INVALID_CONTENTS_SIZE);
        }
    }
}