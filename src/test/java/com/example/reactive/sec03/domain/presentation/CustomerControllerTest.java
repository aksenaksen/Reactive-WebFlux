package com.example.reactive.sec03.domain.presentation;

import com.example.reactive.sec03.dto.CustomerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec03")
class CustomerControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerControllerTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("전체 조회")
    void allCustomers(){
        //given
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("{}", list))
                .hasSize(10);

        //when

        //then
    }


    @Test
    @DisplayName("전체 조회 페이징")
    void allCustomersPaging(){
        //given
        client.get()
                .uri("/customers/paginated?page=1&size=5")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", r.getResponseBody()))
                .jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0].id").isEqualTo(1);

        //when

        //then
    }

}