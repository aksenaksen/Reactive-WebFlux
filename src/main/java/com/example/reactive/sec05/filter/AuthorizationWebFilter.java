package com.example.reactive.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Order(2)
@Component
public class AuthorizationWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var category = exchange.getAttributeOrDefault("category", Category.STANDARD);

        return switch (category){
            case PRIME -> prime(exchange,chain);
            case STANDARD -> standard(exchange,chain);
        };
    }

    public Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }

    public Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
        if(exchange.getRequest().getMethod().equals(HttpMethod.GET)){
            return chain.filter(exchange);
        };
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
    }
}
