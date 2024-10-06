package com.example.foody.state.order;

import com.example.foody.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OrderState {
    private Order order;

    public OrderState(Order order) {
        this.order = order;
    }

    public abstract void prepare();
    public abstract void awaitPayment();
    public abstract void complete();
}
