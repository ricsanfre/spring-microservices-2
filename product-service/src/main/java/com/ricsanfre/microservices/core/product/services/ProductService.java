package com.ricsanfre.microservices.core.product.services;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import com.ricsanfre.microservices.core.product.db.entity.Product;
import com.ricsanfre.microservices.core.product.db.repository.ProductRepository;
import com.ricsanfre.microservices.core.product.mapper.ProductMapper;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ServiceUtil serviceUtil;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            ServiceUtil serviceUtil) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.serviceUtil = serviceUtil;
    }

    public ProductDTO getProduct(int productId) {

        LOG.info("Getting details for productId={}", productId);

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        Product product = productRepository.findByProductId(productId).
                orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        LOG.debug("getProduct: found productId: {}", product.getProductId());

        return productMapper.toProductDTO(product);

    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            Product product = productMapper.toProduct(productDTO);
            Product newProduct = productRepository.save(product);
            LOG.debug("createProduct: created a Product entity: {}", productDTO.productId());
            return productMapper.toProductDTO(newProduct);
        } catch (DuplicateKeyException ex)
        {
            throw new InvalidInputException("Duplicate productId: " + productDTO.productId(), ex);
        }
    }

    public void deleteProduct(int productId) {

        Product product = productRepository.findByProductId(productId).
                orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
        LOG.debug("deleting product: {}", product.getProductId());
        productRepository.delete(product);
    }
}
