package com.example.foody.exceptions.order;

/**
 * Exception thrown when an order is in an invalid state.
 */
public class InvalidOrderStateException extends RuntimeException {

    /**
     * Constructs a new InvalidOrderStateException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidOrderStateException(String message) {
        super(message);
    }
}