package com.ricsanfre.microservices.composite.product.controller;

import com.ricsanfre.microservices.api.composite.ProductAggregate;
import com.ricsanfre.microservices.api.composite.ProductCompositeRestService;
import com.ricsanfre.microservices.composite.product.services.ProductCompositeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCompositeController implements ProductCompositeRestService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeController.class);

    private final ProductCompositeService productCompositeService;

    public ProductCompositeController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }


    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.info("/product API request for productId={}", productId);
        return productCompositeService.getProductAggregate(productId);
    }
}
