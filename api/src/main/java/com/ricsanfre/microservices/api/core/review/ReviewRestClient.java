package com.ricsanfre.microservices.api.core.review;

import com.ricsanfre.microservices.api.actuator.HealthDTO;
import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "review",
        url = "${app.review.url:#{null}}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface ReviewRestClient extends ReviewRestService {

    @GetMapping(
            value = "/actuator/health",
            produces = "application/json")
    HealthDTO getHealth();
}
