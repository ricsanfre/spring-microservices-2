package com.ricsanfre.microservices.api.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAddressesDTO {
    private String compositeAddress;
    private String productAddress;
    private String reviewAddress;
    private String recommendationAddress;

}
