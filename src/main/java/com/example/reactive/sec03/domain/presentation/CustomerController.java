package com.example.reactive.sec03.domain.presentation;

import com.example.reactive.sec03.domain.application.CustomerService;
import com.example.reactive.sec03.domain.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer customerId){
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public Mono<CustomerDto> createCustomer(@RequestBody Mono<CustomerDto> mono){
        return customerService.save(mono);
    }

    @PutMapping("/{customerId}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer customerId,
                                            @RequestBody Mono<CustomerDto> mono){
        return customerService.update(customerId, mono);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable Integer customerId){
        return customerService.delete(customerId);
    }
}
