package com.example.foody.exceptions.dish;

/**
 * Exception thrown when access to a dish is forbidden.
 */
public class ForbiddenDishAccessException extends RuntimeException {

    /**
     * Constructs a new ForbiddenDishAccessException with a default message.
     */
    public ForbiddenDishAccessException() {
        super("Dish access denied.");
    }
}