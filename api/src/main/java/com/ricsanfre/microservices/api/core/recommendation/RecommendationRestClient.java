package com.ricsanfre.microservices.api.core.recommendation;

import com.ricsanfre.microservices.api.actuator.HealthDTO;
import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "recommendation",
        url = "${app.recommendation.url:#{null}}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface RecommendationRestClient extends RecommendationRestService {

    @GetMapping(
            value = "/actuator/health",
            produces = "application/json")
    HealthDTO getHealth();


}
