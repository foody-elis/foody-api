package com.example.foody.state.order;

import com.example.foody.model.Order;

public class AwaitingPaymentState extends OrderState {
    public AwaitingPaymentState(Order order) {
        super(order, OrderStatus.AWAITING_PAYMENT.name());
    }

    @Override
    public void prepare() {
        throw new IllegalStateException("Cannot prepare order that is already awaiting payment.");
    }

    @Override
    public void awaitPayment() {
        throw new IllegalStateException("Order is already awaiting payment");
    }

    @Override
    public void complete() {
        getOrder().setState(new CompletedState(getOrder()));
    }
}
