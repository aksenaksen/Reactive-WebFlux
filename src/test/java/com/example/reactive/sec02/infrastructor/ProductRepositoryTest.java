package com.example.reactive.sec02.infrastructor;

import com.example.reactive.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest extends AbstractTest {

    private final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("가격 찾기 테스트")
    void findPriceBetweenTest(){
        //given
        productRepository.findByPriceBetween(200, 400)
                .doOnNext(p -> log.info("{}",p))
                .as(StepVerifier::create)
                .expectNextCount(4)
                .expectComplete()
                .verify();
        //when

        //then
    }

}