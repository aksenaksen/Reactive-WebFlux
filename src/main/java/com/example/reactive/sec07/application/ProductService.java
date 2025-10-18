package com.example.reactive.sec07.application;

import com.example.reactive.sec07.dto.ProductDto;
import com.example.reactive.sec07.infrastructor.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Sinks.Many<ProductDto> sinks;

    public Mono<ProductDto> save(Mono<ProductDto> mono){
        return mono.map(ProductDto::toEntity)  // ProductDto → Product
                .flatMap(productRepository::save)
                .map(ProductDto::from)
                .doOnNext(sinks::tryEmitNext);// Product → ProductDto
    }
//  as -> fulx 전체 요소를 변환
//  flatMap -> 하나씩 변환

    public Flux<ProductDto> getProductStream(){
        return sinks.asFlux();
    }


}
