package com.ricsanfre.microservices.core.review.mapper;

import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.core.review.db.entity.Review;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// Inject ServiceUtil bean, so it can be used by mapper expressions
// See details here https://www.baeldung.com/mapstruct#2-inject-spring-components-into-the-mapper
@Mapper(
        componentModel = "spring")
public abstract class ReviewMapper {

    @Autowired
    protected ServiceUtil serviceUtil;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    public abstract Review dtoToDao(ReviewDTO dto);

    @Mappings({
            @Mapping(target = "serviceAddress",
                    expression = "java(serviceUtil.getServiceAddress())")
    })
    public abstract ReviewDTO daoToDto(Review dao);

    public abstract List<ReviewDTO> daoListToDtoList(List<Review> dao);

    public abstract List<Review> dtoListToDaoList(List<ReviewDTO> api);
}
