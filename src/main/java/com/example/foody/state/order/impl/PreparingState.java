package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class PreparingState extends OrderState {
    public PreparingState() {
        super(OrderStatus.PREPARING);
    }

    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Order is already being prepared.");
    }

    @Override
    public void awaitPayment(Order order) {
        order.setState(new AwaitingPaymentState());
    }

    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Cannot complete order that hasn't awaited payment yet.");
    }
}
