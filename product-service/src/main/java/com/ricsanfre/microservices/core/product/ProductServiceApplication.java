package com.ricsanfre.microservices.core.product;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;


/*
   SpringBootApplication
   Scan package com.ricsanfre.microservice looking for @Configuration classes
   Not only packages local to this project but packages included in other
   common submodules(i.e: api or util modules)

 */
@SpringBootApplication(
        scanBasePackages = {
                "com.ricsanfre.microservices"
        }
)
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}