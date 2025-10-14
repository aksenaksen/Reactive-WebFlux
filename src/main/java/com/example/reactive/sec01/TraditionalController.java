package com.example.reactive.sec01;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("traditional")
public class TraditionalController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalController.class);

    private final RestClient restClient = RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl("http://localhost:7070")
            .build();

    @GetMapping("/products")
    public List<Product> getProducts(){
        var list = restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>(){
                });
        log.info("received response : {}", list);
        return list;
    }

}
