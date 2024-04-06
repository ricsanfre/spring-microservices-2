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
    public ReviewDTO createReview(ReviewDTO body) {
        LOG.info("Create /review API invoked");
        return reviewService.createReview(body);
    }

    @Override
    public List<ReviewDTO> getReviews(int productId) {

        LOG.info("Get /review API invoked for productId={}", productId);
        return reviewService.getReviews(productId);
    }

    @Override
    public void deleteReviews(int productId) {

        LOG.info("Delete /review API invoked for productId={}", productId);
        reviewService.deleteReviews(productId);
    }
}
