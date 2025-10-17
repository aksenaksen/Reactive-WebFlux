package com.example.reactive.sec04.exception;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFoundException(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName() {
        return Mono.error(new InvalidInputException("이름은 필수값입니다."));
    }

    public static <T> Mono<T> missingValidEmail() {
        return Mono.error(new InvalidInputException("이메일은 필수 값입니다."));
    }
}

