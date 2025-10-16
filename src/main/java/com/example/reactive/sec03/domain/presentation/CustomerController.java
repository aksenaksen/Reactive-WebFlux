package com.example.reactive.sec03.domain.presentation;

import com.example.reactive.sec03.domain.application.CustomerService;
import com.example.reactive.sec03.domain.dto.CustomerDto;
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
    public Mono<ResponseEntity<CustomerDto>> getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<CustomerDto> createCustomer(@RequestBody Mono<CustomerDto> mono) {
        return customerService.save(mono);
    }

    @PutMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable Integer customerId,
                                            @RequestBody Mono<CustomerDto> mono) {
        return customerService.update(customerId, mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customerId}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer customerId) {
        return customerService.delete(customerId)
                .filter(b -> b)
                .map(b -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
