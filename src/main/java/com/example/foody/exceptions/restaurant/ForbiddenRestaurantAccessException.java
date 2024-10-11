package com.example.foody.exceptions.restaurant;

public class ForbiddenRestaurantAccessException extends RuntimeException {
    public ForbiddenRestaurantAccessException() {
        super("Restaurant access denied.");
    }
}
