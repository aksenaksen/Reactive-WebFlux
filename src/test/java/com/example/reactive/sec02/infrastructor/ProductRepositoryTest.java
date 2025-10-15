package com.example.reactive.sec02.infrastructor;

import com.example.reactive.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Test
    @DisplayName("페이징 테스트")
    void findByPageTest(){
        //given
        productRepository.findBy(PageRequest.of(0, 3).withSort(Sort.by("price").ascending()))
                .doOnNext(p -> log.info("{}",p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();
        //when

        //then
    }

}