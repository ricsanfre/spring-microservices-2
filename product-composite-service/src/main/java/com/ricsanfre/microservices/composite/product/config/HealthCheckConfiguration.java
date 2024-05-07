package com.ricsanfre.microservices.composite.product.config;

import com.ricsanfre.microservices.composite.product.services.ProductCompositeIntegration;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
public class HealthCheckConfiguration {

    final private ProductCompositeIntegration productCompositeIntegration;

    public HealthCheckConfiguration(
            ProductCompositeIntegration productCompositeIntegration) {
        this.productCompositeIntegration = productCompositeIntegration;
    }

    @Bean
    HealthContributor coreServices() {
        final Map<String, HealthIndicator> registry = new LinkedHashMap<>();
        registry.put("product", () -> productCompositeIntegration.getProductServiceHealth());
        registry.put("recommendation", () -> productCompositeIntegration.getRecommendationServiceHealth());
        registry.put("review", () -> productCompositeIntegration.getReviewServiceHealth());
        return CompositeHealthContributor.fromMap(registry);

    }

}
