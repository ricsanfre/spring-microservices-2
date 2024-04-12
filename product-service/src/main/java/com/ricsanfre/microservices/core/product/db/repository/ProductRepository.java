package com.ricsanfre.microservices.core.product.db.repository;

import com.ricsanfre.microservices.core.product.db.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {

    Optional<Product> findByProductId(int productId);

}
