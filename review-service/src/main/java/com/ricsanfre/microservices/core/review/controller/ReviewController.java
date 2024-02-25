package com.ricsanfre.microservices.core.review.controller;

import com.ricsanfre.microservices.api.core.review.Review;
import com.ricsanfre.microservices.api.core.review.ReviewRestService;
import com.ricsanfre.microservices.core.review.services.ReviewService;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
public class ReviewController implements ReviewRestService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(
            ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public List<Review> getReviews(int productId) {

        LOG.info("/review API invoked for productId={}", productId);

        return reviewService.getReviews(productId);
    }
}
