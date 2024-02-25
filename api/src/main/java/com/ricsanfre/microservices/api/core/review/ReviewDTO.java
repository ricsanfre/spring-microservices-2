package com.ricsanfre.microservices.api.core.review;

public record ReviewDTO(int productId,
                        int reviewId,
                        String author,
                        String subject,
                        String content,
                        String serviceAddress) {

}
