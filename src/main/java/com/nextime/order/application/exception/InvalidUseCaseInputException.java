package com.nextime.order.application.exception;

public class InvalidUseCaseInputException extends RuntimeException {
    public InvalidUseCaseInputException(String message) {
        super(message);
    }
}
