package com.example.foody.exceptions.order;

/**
 * Exception thrown when an order is not allowed.
 */
public class OrderNotAllowedException extends RuntimeException {

    /**
     * Constructs a new OrderNotAllowedException with a default message indicating that the order is not allowed.
     */
    public OrderNotAllowedException() {
        super("Order not allowed.");
    }

    /**
     * Constructs a new OrderNotAllowedException with a formatted message indicating the restaurant ID that caused the order to be not allowed.
     *
     * @param restaurantId the ID of the restaurant for which the order is not allowed
     */
    public OrderNotAllowedException(long restaurantId) {
        super(String.format("Order not allowed for restaurant with id = %s.", restaurantId));
    }

    /**
     * Constructs a new OrderNotAllowedException with a formatted message indicating the restaurant ID and the cause that made the order not allowed.
     *
     * @param restaurantId the ID of the restaurant for which the order is not allowed
     * @param cause        the cause of the order not being allowed
     */
    public OrderNotAllowedException(long restaurantId, String cause) {
        super(String.format("Order not allowed for restaurant with id = %s. Cause: %s.", restaurantId, cause));
    }
}