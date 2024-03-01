package com.ricsanfre.microservices.core.review.services;

import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ServiceUtil serviceUtil;

    public ReviewService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    public List<ReviewDTO> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if (productId == 213) {
            LOG.debug("No reviews found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<ReviewDTO> list = new ArrayList<>();
        list.add(new ReviewDTO(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
        list.add(new ReviewDTO(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
        list.add(new ReviewDTO(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/reviews response size: {}", list.size());

        return list;
    }
}
