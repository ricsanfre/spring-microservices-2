package com.ricsanfre.microservices.core.review.db.repository;

import com.ricsanfre.microservices.core.review.db.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByProductId(int productId);
}
