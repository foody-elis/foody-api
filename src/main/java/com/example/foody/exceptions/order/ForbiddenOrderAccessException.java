package com.example.foody.exceptions.order;

/**
 * Exception thrown when there is an attempt to access an order that is forbidden.
 */
public class ForbiddenOrderAccessException extends RuntimeException {

    /**
     * Constructs a new ForbiddenOrderAccessException with a default message indicating that order access is denied.
     */
    public ForbiddenOrderAccessException() {
        super("Order access denied.");
    }
}