package com.example.reactive.webclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

public class ErrorResponseTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseTest.class);

    private final WebClient webClient = createWebClient(b -> b.defaultHeader("caller-id" , "hello"));

    @Test
    @DisplayName("")
    void handlingError(){
        //given
        webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation" , "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .onErrorReturn(new CalculatorResponse(0, 0, null, 0.0))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //when

        //then
    }


    @Test
    @DisplayName("")
    void handlingErrorReturnError(){
        //given
        webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation" , "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnError(WebClientResponseException.class, ex -> log.info("{}", ex
                        .getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.BadRequest.class ,new CalculatorResponse(0, 0, null, 0.0))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //when

        //then
    }
}
