package com.ricsanfre.microservices.composite.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.ricsanfre.microservices"
        }
)
@EnableFeignClients(
        basePackages = "com.ricsanfre.microservices.api"
)
public class ProductCompositeServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(ProductCompositeServiceApplication.class, args);
    }
}