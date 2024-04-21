package com.ricsanfre.microservices.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSummaryDTO {
    private int reviewId;
    private String author;
    private String subject;
    private String content;

}
