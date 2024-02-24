package com.ricsanfre.microservices.core.review.controller;

import com.ricsanfre.microservices.api.core.review.Review;
import com.ricsanfre.microservices.api.core.review.ReviewService;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
public class ReviewController implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    private final ServiceUtil serviceUtil;

    public ReviewController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {

        LOG.info("/review API invoked for productId={}", productId);

        List<Review> list = new ArrayList<>();
        list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/reviews response size: {}", list.size());
        return list;
    }
}