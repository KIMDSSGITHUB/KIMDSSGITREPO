package com.dss.config;

import com.dss.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,JwtAuthenticationFilter filter) {

        return builder.routes()
                .route("user", r -> r.path("/user/**").filters(f -> f.filter(filter)).uri("lb://ms-login-service"))
                .route("movie", r -> r.path("/movies/**").filters(f -> f.filter(filter)).uri("lb://ms-movie-service"))
                .route("actor", r -> r.path("/actors/**").filters(f -> f.filter(filter)).uri("lb://ms-actor-service"))
                .route("review", r -> r.path("/reviews/**").filters(f -> f.filter(filter)).uri("lb://ms-review-service"))
                .build();
    }
}
