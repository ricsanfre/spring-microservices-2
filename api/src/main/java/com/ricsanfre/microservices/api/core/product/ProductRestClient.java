package com.ricsanfre.microservices.api.core.product;

import com.ricsanfre.microservices.api.actuator.HealthDTO;
import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "product",
        url = "${app.product.url:#{null}}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface ProductRestClient extends ProductRestService {
    @GetMapping(
            value = "/actuator/health",
            produces = "application/json")
    HealthDTO getHealth();

}
