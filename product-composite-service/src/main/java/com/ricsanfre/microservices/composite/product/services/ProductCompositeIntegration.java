package com.ricsanfre.microservices.composite.product.services;

import com.ricsanfre.microservices.api.actuator.HealthDTO;
import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.product.ProductRestClient;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationRestClient;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.core.review.ReviewRestClient;
import com.ricsanfre.microservices.api.errors.exceptions.ServiceNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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

    public ProductDTO createProduct(ProductDTO productDTO) {
        return productClient.createProduct(productDTO);
    }

    public void deleteProduct(int productId) {

        productClient.deleteProduct(productId);
    }

    public List<RecommendationDTO> getRecommendations(int productId) {
        return recommendationClient.getRecommendations(productId);
    }

    public RecommendationDTO createRecommendation(RecommendationDTO recommendationDTO) {
        return recommendationClient.createRecommendation(recommendationDTO);
    }

    public void deleteRecommendations(int productId) {
        recommendationClient.deleteRecommendations(productId);
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        return reviewClient.createReview(reviewDTO);
    }

    public List<ReviewDTO> getReviews(int productId) {
        return reviewClient.getReviews(productId);
    }

    public void deleteReviews(int productId) {
        reviewClient.deleteReviews(productId);
    }

    public Health getRecommendationServiceHealth()   {
        try {
            if (recommendationClient.getHealth().getStatus().equals("UP")) {
                return new Health.Builder().up().build();
            } else {
                return new Health.Builder().down().build();
            }
        } catch (ServiceNotAvailableException e){
            return new Health.Builder().down().build();

        }
    }

    public Health getReviewServiceHealth()   {

        try {
            if (reviewClient.getHealth().getStatus().equals("UP")) {
                return new Health.Builder().up().build();
            } else {
                return new Health.Builder().down().build();
            }
        } catch (ServiceNotAvailableException e){
            return new Health.Builder().down().build();

        }
    }
    public Health getProductServiceHealth()   {
        try {
            if (productClient.getHealth().getStatus().equals("UP")) {
                return new Health.Builder().up().build();
            } else {
                return new Health.Builder().down().build();
            }
        } catch (ServiceNotAvailableException e){
            return new Health.Builder().down().build();

        }
    }




}
