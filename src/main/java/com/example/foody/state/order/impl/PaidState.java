package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class PaidState extends OrderState {
    public PaidState() {
        super(OrderStatus.PAID);
    }

    @Override
    public void create(Order order) {
        throw new IllegalStateException("Cannot create order that is already paid.");
    }

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Order is already paid.");
    }

    @Override
    public void prepare(Order order) {
        order.setState(new PreparingState());
    }

    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Cannot complete order that hasn't been prepared yet.");
    }
}