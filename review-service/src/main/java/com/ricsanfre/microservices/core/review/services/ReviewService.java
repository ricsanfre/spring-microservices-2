package com.ricsanfre.microservices.core.review.services;

import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.core.review.db.entity.Review;
import com.ricsanfre.microservices.core.review.db.repository.ReviewRepository;
import com.ricsanfre.microservices.core.review.mapper.ReviewMapper;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ServiceUtil serviceUtil;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(
            ServiceUtil serviceUtil,
            ReviewRepository reviewRepository,
            ReviewMapper reviewMapper) {
        this.serviceUtil = serviceUtil;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public List<ReviewDTO> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if (productId == 213) {
            LOG.debug("No reviews found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Review> reviews = reviewRepository.findByProductId(productId);

        List<ReviewDTO> list = reviewMapper.daoListToDtoList(reviews);

        LOG.debug("/reviews response size: {}", list.size());

        return list;
    }

    public ReviewDTO createReview(ReviewDTO reviewDto) {

        try {
            Review review = reviewMapper.dtoToDao(reviewDto);
            Review newReview = reviewRepository.save(review);
            LOG.debug("createReview: created a review entity: {}/{}", reviewDto.productId(), reviewDto.reviewId());
            return reviewMapper.daoToDto(newReview);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new InvalidInputException("Duplicate key, Product Id: " + reviewDto.productId() + ", Review Id:" + reviewDto.reviewId());

        }
    }

    public void deleteReviews(int productId) {

        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        reviewRepository.deleteAll(reviewRepository.findByProductId(productId));

    }
}
