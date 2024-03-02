package com.ricsanfre.microservices.api.composite;

public record RecommendationSummaryDTO(
        int recommendationId,
        String author,
        int rate
) {
}

