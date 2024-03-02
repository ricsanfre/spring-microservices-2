package com.ricsanfre.microservices.composite.product;

import com.ricsanfre.microservices.api.composite.ProductAggregateDTO;
import com.ricsanfre.microservices.api.core.product.ProductDTO;
import com.ricsanfre.microservices.api.core.recommendation.RecommendationDTO;
import com.ricsanfre.microservices.api.core.review.ReviewDTO;
import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import com.ricsanfre.microservices.composite.product.services.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductCompositeServiceApplicationTests {

    private static final int PRODUCT_ID_OK = 1;
    private static final int PRODUCT_ID_NOT_FOUND = 2;
    private static final int PRODUCT_ID_INVALID = 3;

    @Autowired
    private WebTestClient webTestClient;

    private static final String URI = "/product-composite";

    @MockBean
    private ProductCompositeIntegration compositeIntegration;

    @BeforeEach
    void setUp() {

        when(compositeIntegration.getProduct(PRODUCT_ID_OK))
                .thenReturn(new ProductDTO(PRODUCT_ID_OK, "name", 1, "mock-address"));
        when(compositeIntegration.getRecommendations(PRODUCT_ID_OK))
                .thenReturn(singletonList(new RecommendationDTO(PRODUCT_ID_OK, 1, "author", 1, "content", "mock address")));
        when(compositeIntegration.getReviews(PRODUCT_ID_OK))
                .thenReturn(singletonList(new ReviewDTO(PRODUCT_ID_OK, 1, "author", "subject", "content", "mock address")));
        when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND))
                .thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));
        when(compositeIntegration.getProduct(PRODUCT_ID_INVALID))
                .thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID));
    }

    @Test
    void getProductById() {
        int productId = PRODUCT_ID_OK;

        ProductAggregateDTO actual = webTestClient.get()
                .uri(URI + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<ProductAggregateDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(actual.productId())
                .isEqualTo(productId);
        assertThat(actual.recommendations().size())
                .isEqualTo(1);
        assertThat(actual.reviews().size())
                .isEqualTo(1);

    }

    @Test
    void getProductNotFound() {

        int productId = PRODUCT_ID_NOT_FOUND;

        ApiErrorResponse response = webTestClient.get()
                .uri(URI + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(new ParameterizedTypeReference<ApiErrorResponse>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(response.path())
                .isEqualTo(URI + "/" + productId);

        assertThat(response.message())
                .isEqualTo("NOT FOUND: " + productId);

    }

    @Test
    void getInvalidInputNegativeValue() {

        int productId = PRODUCT_ID_INVALID;

        ApiErrorResponse responseBody = webTestClient.get()
                .uri(URI + "/" + productId)
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
                .isEqualTo(URI + "/" + productId);

        assertThat(responseBody.message())
                .isEqualTo("INVALID: " + productId);
    }
}
