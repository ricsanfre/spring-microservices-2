package com.ricsanfre.microservices.core.product;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.core.product.db.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"eureka.client.enabled=false"})
public class ProductServiceApplicationTests extends MongoBaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    private static final String productURI = "/product";

    @BeforeEach
    void setupDb() {
        productRepository.deleteAll();
    }

    @Test
    void getProductById() {

        int productId = 1 ;
        // Check repository is empty
        assertThat(productRepository.findByProductId(productId)).isEmpty();

        // Create 1st review
        ProductDTO review = new ProductDTO(productId, "productName" , productId,"SA");
        ProductDTO productCreated = webTestClient.post()
                .uri(productURI)
                .body(Mono.just(review), ProductDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ProductDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(productCreated.getProductId()).isEqualTo(productId);
        assertThat(productCreated.getName()).isEqualTo("productName");
        assertThat(productCreated.getWeight()).isEqualTo(productId);

        // Check repository has 1 entries
        assertThat(productRepository.findByProductId(productId)).isNotEmpty();

        // Get Product Id
        ProductDTO actual = webTestClient.get()
                .uri(productURI + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ProductDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(actual.getProductId())
                .isEqualTo(productId);
    }

    @Test
    void getProductNotFound() {

        int productId = 13;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(productURI + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(responseBody.getPath())
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.getMessage())
                .isEqualTo("No product found for productId: " + productId);
    }

    @Test
    void getInvalidInputNegativeValue() {

        int productId = -1;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(productURI + "/" + productId)
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
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.getMessage())
                .isEqualTo("Invalid productId: " + productId);
    }

    @Test
    void getProductInvalidParameters() {

        String productId = "no-integer" ;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(productURI + "/" + productId)
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
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.getMessage())
                .isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"no-integer\"");
    }


    private WebTestClient.BodyContentSpec postReviewAndVerify(int productId, HttpStatus expectedStatus) {
        ProductDTO product = new ProductDTO(productId, "Name " + productId, productId, "SA");
        return webTestClient.post()
                .uri(productURI)
                .body(Mono.just(product), ProductDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

}
