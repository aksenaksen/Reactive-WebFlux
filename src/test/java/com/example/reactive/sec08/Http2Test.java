package com.example.reactive.sec08;

import com.example.reactive.sec06.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Http2Test extends AbstractWebClient{

    private final WebClient webClient = createWebClient(b -> {
        var poolSize = 1;
        var provider = ConnectionProvider.builder("vins")
                .lifo()
                .maxConnections(poolSize)
                .build();

        var httpClient = HttpClient.create(provider)
                .protocol(HttpProtocol.H2C)
                .compress(true)
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });
// WebClient 는 최대 500개까지만 요청연결 가능.

    @Test
    @DisplayName("")
    void ConcurrentRequestTest() {
        //given
        var max = 501;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> Assertions.assertEquals(list.size(), max))
                .verifyComplete();
        //when
        //then
    }


    private Mono<ProductDto> getProduct(Integer id){
        return webClient.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }
}
