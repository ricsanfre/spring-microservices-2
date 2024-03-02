package com.ricsanfre.microservices.api.composite;

public record ServiceAddressesDTO(
        String compositeAddress,
        String productAddress,
        String reviewAddress,
        String recommendationAddress) {

}
