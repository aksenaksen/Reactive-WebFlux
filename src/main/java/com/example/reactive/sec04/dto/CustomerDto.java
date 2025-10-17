package com.example.reactive.sec04.dto;

import com.example.reactive.sec04.domain.Customer;

public record CustomerDto(
        Integer id,
        String name,
        String email
) {

    public static Customer toEntity(CustomerDto dto) {
        var customer = new Customer();
        customer.setId(dto.id);
        customer.setName(dto.name);
        customer.setEmail(dto.email);
        return customer;
    }

    public static CustomerDto fromEntity(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }
}
