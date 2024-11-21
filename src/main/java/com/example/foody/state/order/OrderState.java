package com.example.foody.state.order;

import com.example.foody.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OrderState {
    private Order order;
    private String name;

    public OrderState(Order order, String name) {
        this.order = order;
        this.name = name;
    }

    public abstract void prepare();
    public abstract void awaitPayment();
    public abstract void complete();
}