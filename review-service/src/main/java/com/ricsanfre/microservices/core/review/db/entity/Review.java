package com.ricsanfre.microservices.core.review.db.entity;

import jakarta.persistence.*;

import java.util.Objects;

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

    public Review() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id && version == review.version && productId == review.productId && reviewId == review.reviewId && Objects.equals(author, review.author) && Objects.equals(subject, review.subject) && Objects.equals(content, review.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, productId, reviewId, author, subject, content);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", version=" + version +
                ", productId=" + productId +
                ", reviewId=" + reviewId +
                ", author='" + author + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
