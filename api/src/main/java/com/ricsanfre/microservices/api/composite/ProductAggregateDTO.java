package com.ricsanfre.microservices.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAggregateDTO {
    private int productId;
    private String name;
    private int weight;
    private List<RecommendationSummaryDTO> recommendations;
    private List<ReviewSummaryDTO> reviews;
    private ServiceAddressesDTO serviceAddresses;

    public ProductAggregateDTO(int productId, String name, int weight) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
    }
}
