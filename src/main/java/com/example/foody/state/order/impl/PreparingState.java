package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class PreparingState extends OrderState {
    public PreparingState() {
        super(OrderStatus.PREPARING);
    }

    @Override
    public void create(Order order) {
        throw new IllegalStateException("Cannot create order that is already being prepared.");
    }

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Cannot pay for order that is already being prepared.");
    }

    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Order is already being prepared.");
    }

    @Override
    public void complete(Order order) {
        order.setState(new CompletedState());
    }
}