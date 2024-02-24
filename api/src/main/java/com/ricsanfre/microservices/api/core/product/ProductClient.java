package com.ricsanfre.microservices.api.core.product;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "product",
        url = "${app.product.url}"
)
public interface ProductClient extends ProductService{

}
