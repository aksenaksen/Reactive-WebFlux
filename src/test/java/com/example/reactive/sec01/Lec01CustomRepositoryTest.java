package com.example.reactive.sec01;

import com.example.reactive.AbstractTest;
import com.example.reactive.sec02.infrastructor.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec01CustomRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec01CustomRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("findAll Test")
    void findAll(){
        //given
        customerRepository.findAll()
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
        //when

        //then
    }

    @Test
    @DisplayName("findById Test")
    void findById(){
        //given
        customerRepository.findById(2)
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
        //when

        //then
    }

    @Test
    @DisplayName("findByNameTest")
    void findByNameTest(){
        //given
        customerRepository.findByName("mike")
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
        //when

        //then
    }


    @Test
    @DisplayName("findByEmailEndingWith")
    void findByEmailEndingWith(){
        //given
        customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
        //when

        //then
    }



}
