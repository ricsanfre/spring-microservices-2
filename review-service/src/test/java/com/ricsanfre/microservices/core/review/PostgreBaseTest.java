package com.ricsanfre.microservices.core.review;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgreBaseTest {

    @Container
    @ServiceConnection
    private static JdbcDatabaseContainer database =
            new PostgreSQLContainer("postgres:16.2");

    static {
        database.start();
    }

}
