package com.ricsanfre.microservices.composite.product.controller;

import com.ricsanfre.microservices.api.composite.ProductAggregate;
import com.ricsanfre.microservices.api.composite.ProductCompositeService;
import com.ricsanfre.microservices.composite.product.services.ProductAggregationService;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCompositeController implements ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeController.class);

    private final ProductAggregationService productAggregationService;

    public ProductCompositeController(ProductAggregationService productAggregationService) {
        this.productAggregationService = productAggregationService;
    }


    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.info("/product API request for productId={}", productId);
        return productAggregationService.getProductAggregate(productId);
    }
}
