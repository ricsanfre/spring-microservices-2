package com.ricsanfre.microservices.core.product.mapper;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.core.product.db.entity.Product;
import com.ricsanfre.microservices.util.http.ServiceUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected ServiceUtil serviceUtil;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    public abstract Product toProduct(ProductDTO productDTO);

    @Mappings({
            @Mapping(target = "serviceAddress",
                    expression = "java(serviceUtil.getServiceAddress())")
    })
    public abstract ProductDTO toProductDTO(Product product);


    public abstract List<ProductDTO> toProductDTOs(List<Product> products);

}
