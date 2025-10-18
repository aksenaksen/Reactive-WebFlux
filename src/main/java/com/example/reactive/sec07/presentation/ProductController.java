package com.example.reactive.sec07.presentation;

import com.example.reactive.sec07.application.ProductService;
import com.example.reactive.sec07.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @PostMapping
    public Mono<ProductDto> save(@RequestBody Mono<ProductDto> productDto){
        return productService.save(productDto);
    }

    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> stream(@PathVariable Integer maxPrice){
        return productService.getProductStream()
                .filter(dto -> dto.price() <= maxPrice);
    }

}
