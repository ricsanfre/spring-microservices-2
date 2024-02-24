package com.ricsanfre.microservices.api.core.recommendation;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "recommendation",
        url = "${app.recommendation.url}"
)
public interface RecommendationClient extends RecommendationService{
}
