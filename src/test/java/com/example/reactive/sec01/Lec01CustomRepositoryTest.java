package com.example.reactive.sec01;

import com.example.reactive.AbstractTest;
import com.example.reactive.sec02.domain.Customer;
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

    @Test
    @DisplayName("삽입 삭제 테스트")
    void insertAndDeleteCustomerTest(){
        //insert
        var customer = new Customer();
        customer.setName("moon");
        customer.setEmail("moon@gmail.com");
        customerRepository.save(customer)
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(customer.getId()))
                .expectComplete()
                .verify();
        //when
        customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

//        delete
        customerRepository.deleteById(11)
                .then(customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("")
    void updateCustomer(){
        //given
        customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("moon"))
                .flatMap(c -> this.customerRepository.save(c))
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("moon", c.getName()))
                .expectComplete()
                .verify();
        //when

        //then
    }




}
