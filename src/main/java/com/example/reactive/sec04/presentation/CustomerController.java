package com.example.reactive.sec04.presentation;

import com.example.reactive.sec04.application.CustomerService;
import com.example.reactive.sec04.dto.CustomerDto;
import com.example.reactive.sec04.exception.ApplicationExceptions;
import com.example.reactive.sec04.validation.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    public Mono<List<CustomerDto>> allCustomers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        return customerService.getAllCustomers(page, size)
                .collectList();
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundException(customerId));
    }

    @PostMapping
    public Mono<CustomerDto> createCustomer(@RequestBody Mono<CustomerDto> mono)
    {
        return mono.transform(RequestValidator.validate())
                .as(customerService::save);
    }

    @PutMapping("/{customerId}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer customerId,
                                                            @RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(validRequest -> customerService.update(customerId, validRequest))
                .switchIfEmpty(ApplicationExceptions.customerNotFoundException(customerId));
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable Integer customerId) {
        return customerService.delete(customerId)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundException(customerId))
                .then();
    }

}
