package com.ricsanfre.microservices.composite.product.services;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.product.ProductRestClient;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationRestClient;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.core.review.ReviewRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);
    private final ReviewRestClient reviewClient;
    private final ProductRestClient productClient;
    private final RecommendationRestClient recommendationClient;

    public ProductCompositeIntegration(ReviewRestClient reviewClient, ProductRestClient productClient, RecommendationRestClient recommendationClient) {
        this.reviewClient = reviewClient;
        this.productClient = productClient;
        this.recommendationClient = recommendationClient;
    }

    public ProductDTO getProduct(int productId) {
        return productClient.getProduct(productId);
    }

    public List<RecommendationDTO> getRecommendations(int productId) {
        return recommendationClient.getRecommendations(productId);
    }

    public List<ReviewDTO> getReviews(int productId) {
        return reviewClient.getReviews(productId);
    }
}
