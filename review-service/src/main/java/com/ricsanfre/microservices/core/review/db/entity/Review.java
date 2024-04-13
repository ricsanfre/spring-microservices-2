package com.ricsanfre.microservices.core.review.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(
        name = "review",
        indexes = {
                @Index(
                        name = "reviews_unique_idx",
                        unique = true,
                        columnList = "productId,reviewId"
                )}
)
public class Review {
    @Id
    @SequenceGenerator(
            name = "review_id_seq",
            sequenceName = "review_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_id_seq"
    )
    private int id;

    /*
      Using Spring Data JPA Optimistic locking
      https://medium.com/@AlexanderObregon/leveraging-springs-version-for-optimistic-locking-in-jpa-bf4126ebc438
     */
    @Version
    private int version;

    private int productId;
    private int reviewId;
    private String author;
    private String subject;
    private String content;

    public Review(int productId, int reviewId, String author, String subject, String content) {
        this.productId = productId;
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
        this.content = content;
    }

}
