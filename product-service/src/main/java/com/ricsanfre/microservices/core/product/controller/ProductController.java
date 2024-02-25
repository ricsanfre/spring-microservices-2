package com.ricsanfre.microservices.core.product.controller;

import com.ricsanfre.microservices.api.core.product.Product;
import com.ricsanfre.microservices.api.core.product.ProductRestService;
import com.ricsanfre.microservices.core.product.services.ProductService;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductRestService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    @Override
    public Product getProduct(int productId) {
        LOG.info("/product API request for productId={}", productId);
        return productService.getProduct(productId);
    }
}
