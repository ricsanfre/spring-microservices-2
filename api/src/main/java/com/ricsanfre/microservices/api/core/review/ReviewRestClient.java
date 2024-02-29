package com.ricsanfre.microservices.api.core.review;

import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "review",
        url = "${app.review.url}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface ReviewRestClient extends ReviewRestService {
}
