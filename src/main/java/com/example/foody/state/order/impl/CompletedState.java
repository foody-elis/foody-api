package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class CompletedState extends OrderState {
    public CompletedState() {
        super(OrderStatus.COMPLETED);
    }

    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Cannot prepare order that is already completed.");
    }

    @Override
    public void awaitPayment(Order order) {
        throw new IllegalStateException("Cannot await payment for order that is already completed.");
    }

    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Order is already completed.");
    }
}
