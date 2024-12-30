package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class AwaitingPaymentState extends OrderState {
    public AwaitingPaymentState() {
        super(OrderStatus.AWAITING_PAYMENT);
    }

    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Cannot prepare order that is already awaiting payment.");
    }

    @Override
    public void awaitPayment(Order order) {
        throw new IllegalStateException("Order is already awaiting payment.");
    }

    @Override
    public void complete(Order order) {
        order.setState(new CompletedState());
    }
}
