package com.ricsanfre.microservices.core.recommendation.controller;

import com.ricsanfre.microservices.api.core.recommendation.Recommendation;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationRestService;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
public class RecommendationController implements RecommendationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationController.class);

    private final ServiceUtil serviceUtil;

    public RecommendationController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        LOG.info("/recommendation API invoked for productId={}", productId);
        List<Recommendation> list = new ArrayList<>();
        list.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 2, "Author 2", 2, "Content 2", serviceUtil.getServiceAddress()));
        list.add(new Recommendation(productId, 3, "Author 3", 3, "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/recommendation response size: {}", list.size());

        return list;

    }
}
