package com.nextime.order.application.exception;

public class InvalidEventFilterException extends RuntimeException {
    public InvalidEventFilterException(String message) {
        super(message);
    }
}
