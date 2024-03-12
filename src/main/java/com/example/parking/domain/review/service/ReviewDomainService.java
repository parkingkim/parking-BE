package com.example.parking.domain.review.service;

import com.example.parking.support.Association;
import com.example.parking.domain.member.Member;
import com.example.parking.domain.parking.Parking;
import com.example.parking.domain.review.Content;
import com.example.parking.domain.review.Review;
import com.example.parking.domain.review.ReviewRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewDomainService {

    private final ReviewRepository reviewRepository;

    public void validateDuplicateReview(Association<Parking> parkingId, Association<Member> reviewerId) {
        if (reviewRepository.existsByParkingIdAndReviewerId(parkingId, reviewerId)) {
            throw new IllegalStateException("유저가 해당 주차장에 대해 이미 리뷰를 작성하였습니다.");
        }
    }

    public Map<Content, Long> collectByContent(List<Review> reviews) {
        return reviews.stream()
                .map(Review::getContents)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public int calculateTotalReviews(Map<Content, Long> counts) {
        return counts.values().stream()
                .mapToInt(Long::intValue)
                .sum();
    }
}
