package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

/**
 * Represents the completed state of an order.
 * <p>
 * Provides methods to handle the lifecycle of an order in the completed state.
 */
public class CompletedState extends OrderState {

    /**
     * Constructor for CompletedState.
     * <p>
     * Sets the order status to COMPLETED.
     */
    public CompletedState() {
        super(OrderStatus.COMPLETED);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order is already completed.
     *
     * @param order the order to be created
     */
    @Override
    public void create(Order order) {
        throw new IllegalStateException("Cannot create order that is already completed.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order is already completed.
     *
     * @param order the order to be paid
     */
    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Cannot pay for order that is already completed.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order is already completed.
     *
     * @param order the order to be prepared
     */
    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Cannot prepare order that is already completed.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order is already completed.
     *
     * @param order the order to be completed
     */
    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Order is already completed.");
    }
}