package com.example.reactive.sec06.application;

import com.example.reactive.sec06.domain.Product;
import com.example.reactive.sec06.dto.ProductDto;
import com.example.reactive.sec06.infrastructor.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<ProductDto> saveAll(Flux<ProductDto> flux){
        return flux.map(ProductDto::toEntity)  // ProductDto → Product
                .as(productRepository::saveAll)
                .map(ProductDto::from);  // Product → ProductDto
    }

    public Mono<Long> getCount(){
        return productRepository.count();
    }

    public Flux<ProductDto> getAllProduct(){
        return productRepository.findAll()
                .map(ProductDto::from);
    }
}
