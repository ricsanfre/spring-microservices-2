package com.ricsanfre.microservices.api.core.review;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "review",
        url = "${app.review.url}"
)
public interface ReviewRestClient extends ReviewRestService {
}
