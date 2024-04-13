package com.ricsanfre.microservices.core.recommendation.db.repository;

import com.ricsanfre.microservices.core.recommendation.db.entity.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecommendationRepository extends MongoRepository<Recommendation,String> {
    List<Recommendation> findByProductId(int productId);
}
