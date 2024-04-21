package com.ricsanfre.microservices.composite.product.services;

import com.ricsanfre.microservices.api.composite.ProductAggregateDTO;
import com.ricsanfre.microservices.api.composite.RecommendationSummaryDTO;
import com.ricsanfre.microservices.api.composite.ReviewSummaryDTO;
import com.ricsanfre.microservices.api.composite.ServiceAddressesDTO;
import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);

    private final ProductCompositeIntegration productCompositeIntegration;
    private final ServiceUtil serviceUtil;

    public ProductCompositeService(ProductCompositeIntegration productCompositeIntegration, ServiceUtil serviceUtil) {
        this.productCompositeIntegration = productCompositeIntegration;
        this.serviceUtil = serviceUtil;
    }


    public ProductAggregateDTO getProductAggregate(int productId) {

        LOG.info("");
        ProductDTO productDTO = productCompositeIntegration.getProduct(productId);
        List<RecommendationDTO> recommendationDTOS = productCompositeIntegration.getRecommendations(productId);
        List<ReviewDTO> reviewDTOS = productCompositeIntegration.getReviews(productId);
        return createProductAggregate(productDTO, recommendationDTOS, reviewDTOS, serviceUtil.getServiceAddress());
    }

    private ProductAggregateDTO createProductAggregate(
            ProductDTO productDTO,
            List<RecommendationDTO> recommendationDTOS,
            List<ReviewDTO> reviewDTOS,
            String serviceAddress) {

        // 1. Setup product info
        int productId = productDTO.getProductId();
        String name = productDTO.getName();
        int weight = productDTO.getWeight();

        // 2. Copy summary recommendation info, if available
        List<RecommendationSummaryDTO> recommendationSummaries =
                (recommendationDTOS == null) ? null : recommendationDTOS.stream()
                        .map(r -> new RecommendationSummaryDTO(
                                r.getRecommendationId(),
                                r.getAuthor(),
                                r.getRate(),
                                r.getContent()
                        ))
                        .collect(Collectors.toList());

        // 3. Copy summary review info, if available
        List<ReviewSummaryDTO> reviewSummaries =
                (reviewDTOS == null) ? null : reviewDTOS.stream()
                        .map(r -> new ReviewSummaryDTO(
                                r.getReviewId(),
                                r.getAuthor(),
                                r.getSubject(),
                                r.getContent()
                        ))
                        .collect(Collectors.toList());

        // 4. Create info regarding the involved microservices addresses
        String productAddress = productDTO.getServiceAddress();
        String reviewAddress = (reviewDTOS != null && !reviewDTOS.isEmpty()) ? reviewDTOS.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendationDTOS != null && !recommendationDTOS.isEmpty()) ? recommendationDTOS.get(0).getServiceAddress() : "";
        ServiceAddressesDTO serviceAddresses = new ServiceAddressesDTO(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregateDTO(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);

    }

}
