package com.example.foody.exceptions.restaurant;

/**
 * Exception thrown when there is an attempt to access a restaurant that is forbidden.
 */
public class ForbiddenRestaurantAccessException extends RuntimeException {

    /**
     * Constructs a new ForbiddenRestaurantAccessException with a default message indicating that restaurant access is denied.
     */
    public ForbiddenRestaurantAccessException() {
        super("Restaurant access denied.");
    }
}