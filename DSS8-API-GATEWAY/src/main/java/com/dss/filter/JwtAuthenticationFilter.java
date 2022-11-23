package com.dss.filter;

import com.dss.exception.JwtTokenMalformedException;
import com.dss.exception.JwtTokenMissingException;
import com.dss.util.TokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/registration", "/login", "/instance");

        String path = request.getURI().getPath();

        boolean isApiPrivate = apiEndpoints.stream()
                .noneMatch(path::contains);

        if (isApiPrivate) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            try {
                tokenUtil.validateToken(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            Claims claims;
            try {
                claims = tokenUtil.getClaims(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            boolean userHasAccess = !path.contains("user/") || path.contains("user/" + claims.getSubject());

            if(!userHasAccess) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            exchange.getRequest().mutate().header("userId", String.valueOf(claims.getSubject())).build();
        }

        return chain.filter(exchange);
    }
}
