package com.example.reactive.sec02.infrastructor;

import com.example.reactive.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderRepositoryTest extends AbstractTest {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    void getProductsByCustomerName() {

        customerOrderRepository.getProductsByCustomerName("mike")
                .doOnNext(co -> log.info("{}", co))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("")
    void CustomerOrderRepositoryTest(){
        //given
        customerOrderRepository.findOrderDetailsByProduct("iphone 20")
                .doOnNext(dto -> log.info("{}", dto))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.amount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.amount()))
                .verifyComplete();
        //when

        //then
    }

}