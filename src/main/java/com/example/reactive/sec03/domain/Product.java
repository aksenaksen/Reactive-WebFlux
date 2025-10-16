package com.example.reactive.sec03.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("product")
@Getter
@Setter
@ToString
public class Product {

    @Id
    private Integer id;
    private String description;
    private Integer price;

}
