package com.ricsanfre.microservices.api.core.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;

    public ProductDTO(int productId, String name, int weight) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
    }
}
