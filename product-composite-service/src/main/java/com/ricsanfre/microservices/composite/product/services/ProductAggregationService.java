package com.ricsanfre.microservices.composite.product.services;

import com.ricsanfre.microservices.api.composite.ProductAggregate;
import com.ricsanfre.microservices.api.composite.RecommendationSummary;
import com.ricsanfre.microservices.api.composite.ReviewSummary;
import com.ricsanfre.microservices.api.composite.ServiceAddresses;
import com.ricsanfre.microservices.api.core.product.Product;
import com.ricsanfre.microservices.api.core.product.ProductClient;
import com.ricsanfre.microservices.api.core.recommendation.Recommendation;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationClient;
import com.ricsanfre.microservices.api.core.review.Review;
import com.ricsanfre.microservices.api.core.review.ReviewClient;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAggregationService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductAggregationService.class);

    private final ReviewClient reviewClient;
    private final ProductClient productClient;

    private final ServiceUtil serviceUtil;
    private final RecommendationClient recommendationClient;

    public ProductAggregationService(
            ReviewClient reviewClient,
            ProductClient productClient,
            ServiceUtil serviceUtil,
            RecommendationClient recommendationClient) {
        this.reviewClient = reviewClient;
        this.productClient = productClient;
        this.serviceUtil = serviceUtil;
        this.recommendationClient = recommendationClient;
    }

    public ProductAggregate getProductAggregate(int productId) {

        LOG.info("");
        Product product = getProduct(productId);
        List<Recommendation> recommendations = getRecommendations(productId);
        List<Review> reviews = getReviews(productId);
        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            String serviceAddress) {

        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries =
                (recommendations == null) ? null : recommendations.stream()
                        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
                        .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries =
                (reviews == null) ? null : reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
                        .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices addresses
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && !recommendations.isEmpty()) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);

    }

    private Product getProduct(int productId) {
        return productClient.getProduct(productId);
    }

    private List<Recommendation> getRecommendations(int productId) {
        return recommendationClient.getRecommendations(productId);
    }

    private List<Review> getReviews(int productId) {
        return reviewClient.getReviews(productId);
    }
}
