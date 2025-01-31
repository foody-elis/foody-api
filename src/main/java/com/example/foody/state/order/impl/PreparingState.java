package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

/**
 * Represents the state of an order that is being prepared.
 * <p>
 * This state handles the transitions and actions that can be performed on an order in preparation.
 */
public class PreparingState extends OrderState {

    /**
     * Constructs a new PreparingState.
     * <p>
     * Sets the order status to PREPARING.
     */
    public PreparingState() {
        super(OrderStatus.PREPARING);
    }

    /**
     * Throws an IllegalStateException as an order that is already being prepared cannot be created.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void create(Order order) {
        throw new IllegalStateException("Cannot create order that is already being prepared.");
    }

    /**
     * Throws an IllegalStateException as an order that is already being prepared cannot be paid for.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Cannot pay for order that is already being prepared.");
    }

    /**
     * Throws an IllegalStateException as an order that is already being prepared cannot be prepared again.
     *
     * @param order the order on which the action is attempted
     * @throws IllegalStateException if the method is called
     */
    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Order is already being prepared.");
    }

    /**
     * Transitions the order to the CompletedState.
     *
     * @param order the order to be completed
     */
    @Override
    public void complete(Order order) {
        order.setState(new CompletedState());
    }
}