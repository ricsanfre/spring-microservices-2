package com.ricsanfre.microservices.api.composite;

import java.util.List;

public record ProductAggregateDTO(
        int productId,
        String name,

        int weight,
        List<RecommendationSummary> recommendations,
        List<ReviewSummary> reviews,
        ServiceAddresses serviceAddresses
) {

}
