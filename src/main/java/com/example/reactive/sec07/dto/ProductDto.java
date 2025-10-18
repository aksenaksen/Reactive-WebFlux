package com.example.reactive.sec07.dto;

import com.example.reactive.sec07.domain.Product;

public record ProductDto(
        Integer id,
        String description,
        Integer price
) {

    public static Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.id);
        product.setPrice(dto.price);
        product.setDescription(dto.description);
        return product;
    }

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
