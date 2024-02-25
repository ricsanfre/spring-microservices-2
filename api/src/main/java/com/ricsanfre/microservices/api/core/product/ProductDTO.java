package com.ricsanfre.microservices.api.core.product;

public record ProductDTO(
        int productId,
        String name,
        int weight,
        String serviceAddress) {

}
