package com.ricsanfre.microservices.core.review;

import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.core.review.db.repository.ReviewRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "eureka.client.enabled=false",
                "spring.flyway.clean-disabled=false"
        })
public class ReviewServiceApplicationTests extends PostgreBaseTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private Flyway flyway;

    private static final String reviewURI = "/review";


    @BeforeEach
    void setupDb() {
        // Clean database before each test
        // repository.deleteAll();

        // Flyway migration
        // https://maciejwalkowiak.com/blog/spring-boot-flyway-clear-database-integration-tests/
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getReviewByProduct() {
        int productId = 1;

        // Check repository is empty
        assertThat(repository.findByProductId(productId).size()).isEqualTo(0);

        // Create 3 reviews for productId
        postReviewAndVerify(productId, 1, HttpStatus.OK);
        postReviewAndVerify(productId, 2, HttpStatus.OK);
        postReviewAndVerify(productId, 3, HttpStatus.OK);

        // Check repository has 3 entries
        assertThat(repository.findByProductId(productId).size()).isEqualTo(3);


        // Get Reviews

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

        assertThat(reviews.get(0).getProductId()).isEqualTo(productId);
        assertThat(reviews.get(0).getReviewId()).isEqualTo(1);
        assertThat(reviews.get(1).getProductId()).isEqualTo(productId);
        assertThat(reviews.get(1).getReviewId()).isEqualTo(2);
        assertThat(reviews.get(2).getProductId()).isEqualTo(productId);
        assertThat(reviews.get(2).getReviewId()).isEqualTo(3);

    }

    @Test
    void duplicateError() {

        int productId = 1;
        int reviewId = 1;
        // Check repository is empty
        assertThat(repository.findByProductId(productId).size()).isEqualTo(0);

        // Create 1st review
        ReviewDTO review = new ReviewDTO(productId, reviewId, "Author " + reviewId, "Subject " + reviewId, "Content " + reviewId, "SA");
        ReviewDTO reviewCreated = webTestClient.post()
                .uri(reviewURI)
                .body(Mono.just(review), ReviewDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ReviewDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviewCreated.getReviewId()).isEqualTo(reviewId);
        assertThat(reviewCreated.getProductId()).isEqualTo(productId);
        assertThat(reviewCreated.getAuthor()).isEqualTo("Author " + reviewId);
        assertThat(reviewCreated.getSubject()).isEqualTo("Subject " + reviewId);
        assertThat(reviewCreated.getContent()).isEqualTo("Content " + reviewId);

        // Check repository has 1 entries
        assertThat(repository.findByProductId(productId).size()).isEqualTo(1);

        // Try to Insert a duplicate
        ApiErrorResponse error = webTestClient.post()
                .uri(reviewURI)
                .body(Mono.just(review), ReviewDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(error.getPath()).isEqualTo(reviewURI);
        assertThat(error.getMessage())
                .isEqualTo("Duplicate key, Product Id: 1, Review Id:1");

        // Check repository has 1 entries
        assertThat(repository.findByProductId(productId).size()).isEqualTo(1);

    }

    @Test
    void deleteReviews() {

        int productId = 1;
        int reviewId = 1;
        // Check repository is empty
        assertThat(repository.findByProductId(productId).size()).isEqualTo(0);

        // Create 1st review
        ReviewDTO review = new ReviewDTO(productId, reviewId, "Author " + reviewId, "Subject " + reviewId, "Content " + reviewId, "SA");
        ReviewDTO reviewCreated = webTestClient.post()
                .uri(reviewURI)
                .body(Mono.just(review), ReviewDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ReviewDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(reviewCreated.getReviewId()).isEqualTo(reviewId);
        assertThat(reviewCreated.getProductId()).isEqualTo(productId);
        assertThat(reviewCreated.getAuthor()).isEqualTo("Author " + reviewId);
        assertThat(reviewCreated.getSubject()).isEqualTo("Subject " + reviewId);
        assertThat(reviewCreated.getContent()).isEqualTo("Content " + reviewId);

        // Check repository has 1 entries
        assertThat(repository.findByProductId(productId).size()).isEqualTo(1);

        // Delete all Reviews for productId
        webTestClient.delete()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        // Check repository has 1 entries
        assertThat(repository.findByProductId(productId).size()).isEqualTo(0);

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

        assertThat(error.getPath())
                .isEqualTo(reviewURI);

        assertThat(error.getMessage())
                .isEqualTo("Required request parameter 'productId' for method parameter type int is not present");

    }

    @Test
    void getReviewsInvalidParameter() {

        String productId = "nointeger";

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

        assertThat(responseBody.getPath())
                .isEqualTo(reviewURI);

        assertThat(responseBody.getMessage())
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

        assertThat(responseBody.getPath())
                .isEqualTo(reviewURI);

        assertThat(responseBody.getMessage())
                .isEqualTo("Invalid productId: " + productId);

    }

    private WebTestClient.BodyContentSpec postReviewAndVerify(int productId, int reviewId, HttpStatus expectedStatus) {
        ReviewDTO review = new ReviewDTO(productId, reviewId, "Author " + reviewId, "Subject " + reviewId, "Content " + reviewId, "SA");
        return webTestClient.post()
                .uri(reviewURI)
                .body(Mono.just(review), ReviewDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }


    private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(int productId, HttpStatus expectedStatus) {
        return webTestClient.get()
                .uri(reviewURI + "?productId=" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }
}