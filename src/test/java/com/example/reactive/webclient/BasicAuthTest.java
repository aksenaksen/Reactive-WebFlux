package com.example.reactive.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class BasicAuthTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseTest.class);

    private final WebClient webClient = createWebClient(b -> b.defaultHeaders(h -> h.setBasicAuth("java", "secret")));

    @Test
    @DisplayName("")
    void BasicAuthTest(){
        //given
        webClient.get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //when

        //then
    }

    @Test
    @DisplayName("")
    void BearerAuthTest(){
        //given
        webClient.get()
                .uri("/lec08/product/{id}", 1)
                .headers(h -> h.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //when

        //then
    }

    private final WebClient filterClient = createWebClient(b -> b.filter(tokenGenerator()));

    @Test
    @DisplayName("")
    void exchangeFilterTest(){
        //given
        for(int i=1; i<=5; i++) {
            filterClient.get()
                    .uri("/lec09/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .then()
                    .as(StepVerifier::create)
                    .verifyComplete();
        }
        //when

        //then
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (request, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("token: {}", token);
//            request.headers().setBearerAuth(token);
            var modifiedRequest = ClientRequest.from(request)
                    .headers(h -> h.setBearerAuth(token))
                    .build();

            return next.exchange(modifiedRequest);

        };
    }
}
