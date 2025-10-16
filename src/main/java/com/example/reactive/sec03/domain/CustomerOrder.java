package com.example.reactive.sec03.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("customer_order")
@Getter
@Setter
@ToString
public class CustomerOrder {

    @Id
    private UUID id;

    private Integer customerId;

    private Integer productId;

    private Integer amount;

    private Instant orderDate;

}
