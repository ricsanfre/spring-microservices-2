package com.ricsanfre.microservices.core.recommendation;

import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.core.recommendation.db.repository.RecommendationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecommendationServiceApplicationTests extends MongoBaseTest{
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private static final String recommendationURI = "/recommendation";

    @Test
    void getRecommendationByProduct() {
        int productId = 1;
        int recommendationId = 1;

        // Check repository is empty
        assertThat(recommendationRepository.findByProductId(productId)).isEmpty();

        // Create 1st recommendation
        RecommendationDTO recommendation = new RecommendationDTO(productId, recommendationId,"author" , 1,"content", "SA");
        RecommendationDTO recommendationCreated = webTestClient.post()
                .uri(recommendationURI)
                .body(Mono.just(recommendation), RecommendationDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<RecommendationDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(recommendationCreated.getProductId()).isEqualTo(productId);
        assertThat(recommendationCreated.getRecommendationId()).isEqualTo(recommendationId);
        assertThat(recommendationCreated.getRate()).isEqualTo(1);
        assertThat(recommendationCreated.getContent()).isEqualTo("content");
        assertThat(recommendationCreated.getAuthor()).isEqualTo("author");

        // Check repository has 1 entries
        assertThat(recommendationRepository.findByProductId(productId)).isNotEmpty();


        List<RecommendationDTO> reviews = webTestClient.get()
                .uri(recommendationURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<RecommendationDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviews.size())
                .isEqualTo(1);

        assertThat(reviews.get(0).getProductId()).isEqualTo(productId);

    }

    @Test
    void getRecommendationMissingParameter() {
        ApiErrorResponse error =
                webTestClient.get()
                        .uri(recommendationURI)
                        .exchange()
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                        })
                        .returnResult()
                        .getResponseBody();

        assertThat(error.getPath())
                .isEqualTo(recommendationURI);

        assertThat(error.getMessage())
                .isEqualTo("Required request parameter 'productId' for method parameter type int is not present");

    }

    @Test
    void getRecommendationInvalidParameter() {

        String productId ="nointeger";

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(recommendationURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.getPath())
                .isEqualTo(recommendationURI);

        assertThat(responseBody.getMessage())
                .isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"nointeger\"");
    }

    @Test
    void getRecommendationNotFound() {

        int productId = 113;
        List<RecommendationDTO> reviews = webTestClient.get()
                .uri(recommendationURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<RecommendationDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviews).isEmpty();
    }

    @Test
    void getReviewsInvalidParameterNegativeValue() {

        int productId = -1;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(recommendationURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.getPath())
                .isEqualTo(recommendationURI);

        assertThat(responseBody.getMessage())
                .isEqualTo("Invalid productId: " + productId);

    }

}
