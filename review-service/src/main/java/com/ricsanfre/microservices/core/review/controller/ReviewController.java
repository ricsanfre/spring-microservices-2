package com.ricsanfre.microservices.core.review.controller;

import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.core.review.ReviewRestService;
import com.ricsanfre.microservices.core.review.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ReviewDTO> getReviews(int productId) {

        LOG.info("/review API invoked for productId={}", productId);

        return reviewService.getReviews(productId);
    }
}
