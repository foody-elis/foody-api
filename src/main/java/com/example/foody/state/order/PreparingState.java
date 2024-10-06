package com.example.foody.state.order;

import com.example.foody.model.Order;

public class PreparingState extends OrderState {
    public PreparingState(Order order) {
        super(order);
    }

    @Override
    public void prepare() {
        throw new IllegalStateException("Order is already being prepared.");
    }

    @Override
    public void awaitPayment() {
        getOrder().setStatus(OrderStatus.AWAITING_PAYMENT);
        getOrder().setState(new AwaitingPaymentState(getOrder()));
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete order that hasn't awaited payment yet.");
    }
}
