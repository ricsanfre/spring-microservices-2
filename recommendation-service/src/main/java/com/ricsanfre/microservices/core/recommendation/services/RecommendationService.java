package com.ricsanfre.microservices.core.recommendation.services;

import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.core.recommendation.db.entity.Recommendation;
import com.ricsanfre.microservices.core.recommendation.db.repository.RecommendationRepository;
import com.ricsanfre.microservices.core.recommendation.mapper.RecommendationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    public RecommendationService(RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    public List<RecommendationDTO> getRecommendations(int productId) {

        LOG.info("/recommendation get API invoked for productId={}", productId);

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        List<Recommendation> recommendations = recommendationRepository.findByProductId(productId);
        List<RecommendationDTO> list = recommendationMapper.toRecommendationDTOs(recommendations);
        LOG.debug("/recommendations response size: {}", list.size());
        return list;

    }

    public RecommendationDTO createRecommendation(RecommendationDTO recommendationDTO) {

        try {
            Recommendation recommendation = recommendationMapper.toRecommendation(recommendationDTO);
            Recommendation newRecommendation = recommendationRepository.save(recommendation);
            LOG.debug("createRecommendation: created a recommendation entity: {}/{}", recommendationDTO.getProductId(), recommendationDTO.getRecommendationId());
            return recommendationMapper.toRecommendationDTO(newRecommendation);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new InvalidInputException("Duplicate key, Product Id: " + recommendationDTO.getProductId() + ", Review Id:" + recommendationDTO.getRecommendationId());
        }
    }

    public void deleteRecommendations(int productId) {
        LOG.debug("deleteRecommendations: tries to delete recommendations for the product with productId: {}", productId);
        recommendationRepository.deleteAll(recommendationRepository.findByProductId(productId));
    }
}
