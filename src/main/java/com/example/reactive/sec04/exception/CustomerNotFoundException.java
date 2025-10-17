package com.example.reactive.sec04.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당하는 고객 [id = %d]을 찾을 수 없습니다";

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }

}
