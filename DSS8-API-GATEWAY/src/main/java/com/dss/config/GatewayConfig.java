package com.dss.config;

import com.dss.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user", r -> r.path("/user/**").filters(f -> f.filter(filter)).uri("lb://ms-login-service"))
                .route("movie", r -> r.path("/movies/**").filters(f -> f.filter(filter)).uri("lb://ms-movie-service"))
                .route("review", r -> r.path("/reviews/**").filters(f -> f.filter(filter)).uri("http://localhost:9008"))
                .build();
    }
}
