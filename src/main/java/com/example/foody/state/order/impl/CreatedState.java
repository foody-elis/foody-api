package com.example.foody.state.order.impl;

import com.example.foody.model.Order;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;

public class CreatedState extends OrderState {
    public CreatedState() {
        super(OrderStatus.CREATED);
    }

    @Override
    public void create(Order order) {
        throw new IllegalStateException("Order is already created.");
    }

    @Override
    public void pay(Order order) {
        order.setState(new PaidState());
    }

    @Override
    public void prepare(Order order) {
        throw new IllegalStateException("Cannot prepare order that hasn't been paid yet.");
    }

    @Override
    public void complete(Order order) {
        throw new IllegalStateException("Cannot complete order that hasn't been paid yet.");
    }
}