package com.example.reactive.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec02FluxTest extends AbstractWebClient{

    private final WebClient webClient = createWebClient();


    @Test
    @DisplayName("")
    void streamingResponse() throws InterruptedException {
        //given
        webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

        //when
        //then
    }

    @Test
    @DisplayName("")
    void streamingResponseTake() throws InterruptedException {
        //given
        webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

        //when
        //then
    }

    @Test
    @DisplayName("")
    void streamingResponseSubscribe() throws InterruptedException {
        //given
        webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(print())
                .subscribe();
        //when
        //then
    }

}
