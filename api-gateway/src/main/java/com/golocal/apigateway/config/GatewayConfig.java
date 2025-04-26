package com.golocal.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Optional configuration for defining gateway routes programmatically.
 * Routes defined here supplement or override routes defined in application.yml.
 * Useful for more complex routing logic or dynamic route creation.
 */
@Configuration
public class GatewayConfig {

    // Example of defining routes programmatically (commented out by default)
    // This is an alternative or addition to defining routes in application.yml

    /*
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Example: Route requests for /alt-products/** to product-service, rewriting the path
            .route("product-service-alternative-path", r -> r
                .path("/api/alt-products/**") // Match alternative path
                .filters(f -> f
                    // Remove the /api/alt-products prefix before forwarding
                    .rewritePath("/api/alt-products/(?<segment>.*)", "/api/products/${segment}")
                    // Add a request header
                    .addRequestHeader("X-Source", "Alternative-Route")
                )
                .uri("lb://product-service") // Forward to product-service
            )
            // Example: Route to user service with a specific filter
            .route("user-service-special", r -> r
                .path("/api/special-users/**")
                .filters(f -> f
                    // Apply custom filter logic if needed
                    // .filter(myCustomFilter.apply(new MyCustomFilter.Config()))
                    .addRequestHeader("X-Route-Type", "Special")
                )
                .uri("lb://user-service")
            )
            .build();
    }
    */

    // If all routes are defined in application.yml, this class can remain empty
    // or be removed.
}