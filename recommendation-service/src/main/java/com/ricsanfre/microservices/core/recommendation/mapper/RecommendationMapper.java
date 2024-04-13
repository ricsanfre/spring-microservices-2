package com.ricsanfre.microservices.core.recommendation.mapper;

import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.core.recommendation.db.entity.Recommendation;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring")
public abstract class RecommendationMapper {

    @Autowired
    protected ServiceUtil serviceUtil;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    public abstract Recommendation toRecommendation(RecommendationDTO recommendationDTO);

    @Mappings({
            @Mapping(target = "serviceAddress",
                    expression = "java(serviceUtil.getServiceAddress())")
    })
    public abstract RecommendationDTO toRecommendationDTO(Recommendation recommendation);

    public abstract List<RecommendationDTO> toRecommendationDTOs(List<Recommendation> recommendations);

}
