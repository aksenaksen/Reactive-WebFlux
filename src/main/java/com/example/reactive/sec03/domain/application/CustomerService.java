package com.example.reactive.sec03.domain.application;

import com.example.reactive.sec03.domain.dto.CustomerDto;
import com.example.reactive.sec03.domain.infrastructor.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers(){
        return customerRepository.findAll()
                .map(CustomerDto::fromEntity);
    }

    public Flux<CustomerDto> getAllCustomers(Integer page, Integer size){
        return customerRepository.findBy(PageRequest.of(page - 1 , size))
                .map(CustomerDto::fromEntity);
    }

    public Mono<CustomerDto> getCustomerById(Integer id){
        return customerRepository.findById(id)
                .map(CustomerDto::fromEntity);
    }

    public Mono<CustomerDto> save(Mono<CustomerDto> mono){
        return mono.map(CustomerDto::toEntity)
                .flatMap(customerRepository::save)
                .map(CustomerDto::fromEntity);
    }

    public Mono<CustomerDto> update(Integer id, Mono<CustomerDto> mono){
        return customerRepository.findById(id)
                .flatMap(entity -> mono)
                .map(CustomerDto::toEntity)
                .doOnNext(c -> c.setId(id))
                .flatMap(customerRepository::save)
                .map(CustomerDto::fromEntity);
    }

    public Mono<Boolean> delete(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }



}
