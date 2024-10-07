package com.example.foody.state.order;

import com.example.foody.model.Order;

public class CompletedState extends OrderState {
    public CompletedState(Order order) {
        super(order, OrderStatus.COMPLETED.name());
    }

    @Override
    public void prepare() {
        throw new IllegalStateException("Cannot prepare order that is already completed.");
    }

    @Override
    public void awaitPayment() {
        throw new IllegalStateException("Cannot await payment for order that is already completed.");
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Order is already completed");
    }
}
