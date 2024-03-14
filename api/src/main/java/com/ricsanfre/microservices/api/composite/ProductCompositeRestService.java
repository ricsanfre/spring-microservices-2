package com.ricsanfre.microservices.api.composite;

import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "ProductComposite", description =
        "REST API for composite product information.")
public interface ProductCompositeRestService {
    /**
     * Sample usage: "curl $HOST:$PORT/product-composite/1".
     *
     * @param productId Id of the product
     * @return the composite product info, if found, else null
     */
    @Operation(
            summary = "${api.product-composite.get-composite-product.description}",
            description = "${api.product-composite.get-composite-product.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.responseCodes.ok.description}",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductAggregateDTO.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "${api.responseCodes.badRequest.description}",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "${api.responseCodes.notFound.description}",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = "application/json")
    ProductAggregateDTO getProduct(@PathVariable("productId") int productId);

}
