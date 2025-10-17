package com.example.reactive.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec1MonoTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    @DisplayName("")
    void simpleGet() throws InterruptedException {
        //given
        client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        //when
        Thread.sleep(Duration.ofSeconds(2));
        //then
    }

    @Test
    @DisplayName("여러요청을 한번에 테스트")
    void concurrentRequests() throws InterruptedException {
        //given
        for (int i = 1; i <= 5; i++) {
            client.get()
                    .uri("/lec01/product/" + i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }
        //when
        Thread.sleep(Duration.ofSeconds(2));
        //then
    }
}