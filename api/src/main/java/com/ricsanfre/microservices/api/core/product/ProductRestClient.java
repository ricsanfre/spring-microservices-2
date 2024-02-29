package com.ricsanfre.microservices.api.core.product;

import com.ricsanfre.microservices.api.errors.RetrieveMessageErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "product",
        url = "${app.product.url}",
        configuration = {RetrieveMessageErrorDecoder.class}
)
public interface ProductRestClient extends ProductRestService {

}
