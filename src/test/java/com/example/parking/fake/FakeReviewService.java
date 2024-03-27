package com.example.parking.fake;

import com.example.parking.application.review.ReviewService;
import com.example.parking.domain.review.ReviewRepository;
import com.example.parking.domain.review.service.ReviewDomainService;

public class FakeReviewService extends ReviewService {
    public FakeReviewService(ReviewRepository reviewRepository,
                             ReviewDomainService reviewDomainService) {
        super(reviewRepository, reviewDomainService);
    }
}
