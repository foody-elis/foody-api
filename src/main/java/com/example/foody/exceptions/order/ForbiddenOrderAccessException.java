package com.example.foody.exceptions.order;

public class ForbiddenOrderAccessException extends RuntimeException {
    public ForbiddenOrderAccessException() {
        super("Order access denied.");
    }
}
