package com.ricsanfre.microservices.core.review.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DO NOT use @Data or @toString with JPA
 * https://medium.com/@miguelangelperezdiaz444/the-hidden-dangers-of-lombok-annotations-in-your-java-code-what-you-need-to-know-8acdce2d6b89
 */
@Getter
@Setter
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
    @Column(
            columnDefinition = "BIGSERIAL"
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
