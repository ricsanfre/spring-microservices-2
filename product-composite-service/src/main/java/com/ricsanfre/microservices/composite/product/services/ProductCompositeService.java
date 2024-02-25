package com.ricsanfre.microservices.composite.product.services;

import com.ricsanfre.microservices.api.composite.ProductAggregateDTO;
import com.ricsanfre.microservices.api.composite.RecommendationSummary;
import com.ricsanfre.microservices.api.composite.ReviewSummary;
import com.ricsanfre.microservices.api.composite.ServiceAddresses;
import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.product.ProductRestClient;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationRestClient;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.core.review.ReviewRestClient;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);

    private final ReviewRestClient reviewClient;
    private final ProductRestClient productClient;

    private final ServiceUtil serviceUtil;
    private final RecommendationRestClient recommendationClient;

    public ProductCompositeService(
            ReviewRestClient reviewClient,
            ProductRestClient productClient,
            ServiceUtil serviceUtil,
            RecommendationRestClient recommendationClient) {
        this.reviewClient = reviewClient;
        this.productClient = productClient;
        this.serviceUtil = serviceUtil;
        this.recommendationClient = recommendationClient;
    }

    public ProductAggregateDTO getProductAggregate(int productId) {

        LOG.info("");
        ProductDTO productDTO = getProduct(productId);
        List<RecommendationDTO> recommendationDTOS = getRecommendations(productId);
        List<ReviewDTO> reviewDTOS = getReviews(productId);
        return createProductAggregate(productDTO, recommendationDTOS, reviewDTOS, serviceUtil.getServiceAddress());
    }

    private ProductAggregateDTO createProductAggregate(
            ProductDTO productDTO,
            List<RecommendationDTO> recommendationDTOS,
            List<ReviewDTO> reviewDTOS,
            String serviceAddress) {

        // 1. Setup product info
        int productId = productDTO.productId();
        String name = productDTO.name();
        int weight = productDTO.weight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries =
                (recommendationDTOS == null) ? null : recommendationDTOS.stream()
                        .map(r -> new RecommendationSummary(r.recommendationId(), r.author(), r.rate()))
                        .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries =
                (reviewDTOS == null) ? null : reviewDTOS.stream()
                        .map(r -> new ReviewSummary(r.reviewId(), r.author(), r.subject()))
                        .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices addresses
        String productAddress = productDTO.serviceAddress();
        String reviewAddress = (reviewDTOS != null && !reviewDTOS.isEmpty()) ? reviewDTOS.get(0).serviceAddress() : "";
        String recommendationAddress = (recommendationDTOS != null && !recommendationDTOS.isEmpty()) ? recommendationDTOS.get(0).serviceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregateDTO(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);

    }

    private ProductDTO getProduct(int productId) {
        return productClient.getProduct(productId);
    }

    private List<RecommendationDTO> getRecommendations(int productId) {
        return recommendationClient.getRecommendations(productId);
    }

    private List<ReviewDTO> getReviews(int productId) {
        return reviewClient.getReviews(productId);
    }
}
