package com.example.reactive.sec02.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("customer")
@Getter
@Setter
@ToString
public class Customer {

    @Id
    Integer id;

    String name;

    String email;
}
