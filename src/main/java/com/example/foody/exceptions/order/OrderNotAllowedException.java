package com.example.foody.exceptions.order;

public class OrderNotAllowedException extends RuntimeException {
    public OrderNotAllowedException() {
        super("Order not allowed.");
    }

    public OrderNotAllowedException(long restaurantId) {
        super(String.format("Order not allowed for restaurant with id = %s.", restaurantId));
    }

    public OrderNotAllowedException(long restaurantId, String cause) {
        super(String.format("Order not allowed for restaurant with id = %s. Cause: %s.", restaurantId, cause));
    }
}
