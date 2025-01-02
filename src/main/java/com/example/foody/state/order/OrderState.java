package com.example.foody.state.order;

import com.example.foody.model.Order;
import com.example.foody.utils.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OrderState {
    private OrderStatus status;

    public OrderState(OrderStatus status) {
        this.status = status;
    }

    public abstract void create(Order order);
    public abstract void pay(Order order);
    public abstract void prepare(Order order);
    public abstract void complete(Order order);
}