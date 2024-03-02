package com.ricsanfre.microservices.core.product;

import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    private static final String productURI = "/product";

    @Test
    void getProductById() {

        int productId = 1;

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

        assertThat(actual.productId())
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

        assertThat(responseBody.path())
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.message())
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

        assertThat(responseBody.path())
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.message())
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

        assertThat(responseBody.path())
                .isEqualTo(productURI + "/" + productId);

        assertThat(responseBody.message())
                .isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"no-integer\"");
    }

}
