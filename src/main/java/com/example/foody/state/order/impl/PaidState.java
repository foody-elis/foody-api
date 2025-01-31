package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

/**
 * Represents the state of an order that has been paid.
 * <p>
 * This state handles the transitions and actions that can be performed on a paid order.
 */
public class PaidState extends OrderState {

    /**
     * Constructs a new PaidState.
     * <p>
     * Sets the order status to PAID.
     */
    public PaidState() {
        super(OrderStatus.PAID);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as an order that is already paid cannot be created.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void create(Order order) {
        throw new IllegalStateException("Cannot create order that is already paid.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as an order that is already paid cannot be paid again.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Order is already paid.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Transitions the order to the PreparingState.
     *
     * @param order the order to be prepared
     */
    @Override
    public void prepare(Order order) {
        order.setState(new PreparingState());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as an order that hasn't been prepared cannot be completed.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Cannot complete order that hasn't been prepared yet.");
    }
}