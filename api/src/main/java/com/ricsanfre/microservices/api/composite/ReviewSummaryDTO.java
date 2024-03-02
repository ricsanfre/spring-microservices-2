package com.ricsanfre.microservices.api.composite;

public record ReviewSummaryDTO(
        int reviewId,
        String author,
        String subject
) {

}
