package com.example.foody.utils.state;

import com.example.foody.state.order.OrderState;
import com.example.foody.state.order.impl.CompletedState;
import com.example.foody.state.order.impl.CreatedState;
import com.example.foody.state.order.impl.PaidState;
import com.example.foody.state.order.impl.PreparingState;
import com.example.foody.utils.enums.OrderStatus;

/**
 * Utility class for handling order states.
 */
public class OrderStateUtils {

    /**
     * Returns the appropriate OrderState instance based on the given OrderStatus.
     *
     * @param status the order status
     * @return the corresponding OrderState instance
     */
    public static OrderState getState(OrderStatus status) {
        return switch (status) {
            case CREATED -> new CreatedState();
            case PAID -> new PaidState();
            case PREPARING -> new PreparingState();
            case COMPLETED -> new CompletedState();
        };
    }
}