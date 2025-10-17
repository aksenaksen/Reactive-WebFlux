package com.example.reactive.webclient;

public record CalculatorResponse(
        Integer first,
        Integer second,
        String operation,
        Double result
) {
}
