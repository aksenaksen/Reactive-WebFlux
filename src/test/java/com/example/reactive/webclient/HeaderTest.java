package com.example.reactive.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class HeaderTest extends AbstractWebClient{
    private final WebClient webClient = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    @DisplayName("")
     void defaultHeader(){
        //given
        webClient.get()
                .uri("/lec04/product/{id}", 1)
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
    void overrideHeader(){
        //given
        webClient.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "new-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //when

        //then
    }

}
