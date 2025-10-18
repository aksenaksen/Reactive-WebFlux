package com.example.reactive.sec06.presentation;

import com.example.reactive.sec06.application.ProductService;
import com.example.reactive.sec06.domain.Product;
import com.example.reactive.sec06.dto.ProductDto;
import com.example.reactive.sec06.dto.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(
            @RequestBody Flux<ProductDto> flux
    ){
        log.info("invoked");
        return productService.saveAll(flux)
                .then(productService.getCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProducts(){
        log.info("requested");
        return productService.getAllProduct();
    }


}
