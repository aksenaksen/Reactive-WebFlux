package com.example.reactive.sec08;

public record CalculatorResponse(
        Integer first,
        Integer second,
        String operation,
        Double result
) {
}
