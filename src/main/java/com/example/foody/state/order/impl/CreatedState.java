package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

/**
 * Represents the created state of an order.
 * <p>
 * Provides methods to handle the lifecycle of an order in the created state.
 */
public class CreatedState extends OrderState {

    /**
     * Constructor for CreatedState.
     * <p>
     * Sets the order status to CREATED.
     */
    public CreatedState() {
        super(OrderStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order is already created.
     *
     * @param order the order to be created
     */
    @Override
    public void create(Order order) {
        throw new IllegalStateException("Order is already created.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Changes the state of the order to PaidState.
     *
     * @param order the order to be paid
     */
    @Override
    public void pay(Order order) {
        order.setState(new PaidState());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order hasn't been paid yet.
     *
     * @param order the order to be prepared
     */
    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Cannot prepare order that hasn't been paid yet.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the order hasn't been paid yet.
     *
     * @param order the order to be completed
     */
    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Cannot complete order that hasn't been paid yet.");
    }
}