package com.example.foody.utils.state;

import com.example.foody.state.order.OrderState;
import com.example.foody.state.order.impl.CompletedState;
import com.example.foody.state.order.impl.CreatedState;
import com.example.foody.state.order.impl.PaidState;
import com.example.foody.state.order.impl.PreparingState;
import com.example.foody.utils.enums.OrderStatus;

public class OrderStateUtils {
    public static OrderState getState(OrderStatus status) {
        return switch (status) {
            case CREATED -> new CreatedState();
            case PAID -> new PaidState();
            case PREPARING -> new PreparingState();
            case COMPLETED -> new CompletedState();
        };
    }
}