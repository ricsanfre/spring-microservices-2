package com.ricsanfre.microservices.core.product.services;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final ServiceUtil serviceUtil;

    public ProductService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    public ProductDTO getProduct(int productId) {

        LOG.info("Getting details for productId={}", productId);

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        if (productId == 13) {
            throw new NotFoundException("No product found for productId: " + productId);
        }
        return new ProductDTO(productId, "name-" + productId, 123,
                serviceUtil.getServiceAddress());
    }
}
