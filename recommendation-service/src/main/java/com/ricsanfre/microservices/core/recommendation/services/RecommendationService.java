package com.ricsanfre.microservices.core.recommendation.services;

import com.ricsanfre.microservices.api.core.recommendation.Recommendation;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

    private final ServiceUtil serviceUtil;

    public RecommendationService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    public List<Recommendation> getRecommendations(int productId) {

        LOG.info("/recommendation API invoked for productId={}", productId);

        if (productId < 1) {
            throw new IllegalArgumentException("Invalid productId: " + productId);
        }

        if (productId == 113) {
            LOG.debug("No recommendations found for productId: {}", productId);
            return new ArrayList<>();
        }

        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 3, "Author 3", 3, "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/recommendation response size: {}", list.size());

        return list;
    }
}
