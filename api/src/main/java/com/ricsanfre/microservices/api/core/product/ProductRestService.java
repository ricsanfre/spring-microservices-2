package com.ricsanfre.microservices.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductRestService {

    /**
     * Sample usage: "curl $HOST:$PORT/product/1".
     *
     * @param productId Id of the product
     * @return the product, if found, else null
     */

    @GetMapping(
            value = "/product/{productId}"
    )
    ProductDTO getProduct(@PathVariable("productId") int productId);
}
