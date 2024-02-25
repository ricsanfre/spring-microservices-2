package com.ricsanfre.microservices.core.recommendation.controller;

import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationRestService;
import com.ricsanfre.microservices.core.recommendation.services.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class RecommendationController implements RecommendationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public List<RecommendationDTO> getRecommendations(int productId) {
        return recommendationService.getRecommendations(productId);

    }
}
