package com.ricsanfre.microservices.api.core.recommendation;

import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "recommendation",
        url = "${app.recommendation.url}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface RecommendationRestClient extends RecommendationRestService {
}
