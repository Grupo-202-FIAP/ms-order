package com.nextime.order.application.exception;

public class UseCaseExecutionException extends RuntimeException {
    public UseCaseExecutionException(String message) {
        super(message);
    }
}
