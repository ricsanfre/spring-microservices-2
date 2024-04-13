package com.ricsanfre.microservices.core.recommendation.db.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="recommendations")
@CompoundIndex(
        name = "prod-rec-id",
        unique = true, def = "{'productId': 1, 'recommendationId' : 1}"
)
@Data
@NoArgsConstructor
public class Recommendation {

    @Id
    private String id;

    @Version
    private int version;

    private int productId;
    private int recommendationId;
    private String author;
    private int rate;
    private String content;

    public Recommendation(int productId, int recommendationId, String author, int rate, String content) {
        this.productId = productId;
        this.recommendationId = recommendationId;
        this.author = author;
        this.rate = rate;
        this.content = content;
    }
}
