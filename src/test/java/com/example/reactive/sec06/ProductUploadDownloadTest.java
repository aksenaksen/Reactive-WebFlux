package com.example.reactive.sec06;

import com.example.reactive.sec06.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ProductUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();

    @Test
    @DisplayName("")
    void upload(){
        //given
        var flux = Flux.just(new ProductDto(null, "iphone", 1000))
                .delayElements(Duration.ofSeconds(10));
        //when
        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("{}" , r.toString()))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //then
    }

    @Test
    @DisplayName("")
    void uploadStream(){
        //given
        var flux = Flux.range(1,1_000)
                .map(i -> new ProductDto(null, "iphone" + i, i));
        //when
        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("{}" , r.toString()))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //then
    }

    @Test
    @DisplayName("")
    void downloadStream() throws InterruptedException {

        var flux = Flux.range(1,10)
                .map(i -> new ProductDto(null, "iphone" + i, i));
        //when
        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("upload {}" , r.toString()))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //given
        //when
        productClient.downloadProducts()
                .doOnNext(r -> log.info("download {}" , r.toString()))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
        //then
    }
}
