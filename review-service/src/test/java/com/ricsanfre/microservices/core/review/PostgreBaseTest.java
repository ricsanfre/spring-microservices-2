package com.ricsanfre.microservices.core.review;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;

abstract public class PostgreBaseTest {

    private static JdbcDatabaseContainer database =
            new PostgreSQLContainer("postgres:16.2")
                    .withDatabaseName("review-unit-test")
                    .withUsername("review")
                    .withPassword("s1cret0");

    static {
        database.start();
    }

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                ()-> database.getJdbcUrl());
        registry.add("spring.datasource.username",
                ()-> database.getUsername());
        registry.add("spring.datasource.password",
                ()-> database.getPassword());

    }

}
