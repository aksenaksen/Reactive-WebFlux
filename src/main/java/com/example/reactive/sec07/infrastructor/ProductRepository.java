package com.example.reactive.sec07.infrastructor;

import com.example.reactive.sec07.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(Integer low, Integer high);

    Flux<Product> findBy(Pageable pageable);

}
