package com.example.foody.state.order;

import com.example.foody.model.Order;
import com.example.foody.utils.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing the state of an order.
 * <p>
 * Provides methods to handle the lifecycle of an order.
 */
@Getter
@Setter
public abstract class OrderState {

    private OrderStatus status;

    /**
     * Constructor for OrderState.
     *
     * @param status the status of the order
     */
    public OrderState(OrderStatus status) {
        this.status = status;
    }

    /**
     * Creates the order.
     *
     * @param order the order to be created
     */
    public abstract void create(Order order);

    /**
     * Pays for the order.
     *
     * @param order the order to be paid
     */
    public abstract void pay(Order order);

    /**
     * Prepares the order.
     *
     * @param order the order to be prepared
     */
    public abstract void prepare(Order order);

    /**
     * Completes the order.
     *
     * @param order the order to be completed
     */
    public abstract void complete(Order order);
}