package com.ricsanfre.microservices.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationSummaryDTO {
    private int recommendationId;
    private String author;
    private int rate;
    private String content;
}

