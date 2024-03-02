package com.ricsanfre.microservices.core.review;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewServiceApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    private static final String reviewURI = "/review";

    @Test
    void getReviewByProduct() {
        int productId = 1;
        List<ReviewDTO> reviews = webTestClient.get()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<ReviewDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviews.size())
                .isEqualTo(3);

        assertThat(reviews.get(0).productId()).isEqualTo(productId);

    }

    @Test
    void getReviewsMissingParameter() {
        ApiErrorResponse error =
                webTestClient.get()
                        .uri(reviewURI)
                        .exchange()
                        .expectStatus()
                        .isBadRequest()
                        .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                        })
                        .returnResult()
                        .getResponseBody();

        assertThat(error.path())
                .isEqualTo(reviewURI);

        assertThat(error.message())
                .isEqualTo("Required request parameter 'productId' for method parameter type int is not present");

    }

    @Test
    void getReviewsInvalidParameter() {

        String productId ="nointeger";

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.path())
                .isEqualTo(reviewURI);

        assertThat(responseBody.message())
                .isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"nointeger\"");
    }

    @Test
    void getReviewsNotFound() {

        int productId = 213;
        List<ReviewDTO> reviews = webTestClient.get()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<ReviewDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviews).isEmpty();
    }

    @Test
    void getReviewsInvalidParameterNegativeValue() {

        int productId = -1;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.path())
                .isEqualTo(reviewURI);

        assertThat(responseBody.message())
                .isEqualTo("Invalid productId: " + productId);

    }
}