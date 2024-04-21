package com.ricsanfre.microservices.core.recommendation;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MongoBaseTest {
    @Container
    @ServiceConnection
    private final static MongoDBContainer database = new MongoDBContainer("mongo:7.0.8");

    static {
        database.start();
    }
}
