package com.example.foody.model.order_dish;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key for the OrderDish entity.
 */
@Data
@NoArgsConstructor
@Embeddable
public class OrderDishKey implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "dish_id")
    private Long dishId;

    /**
     * Constructs an OrderDishKey with the specified order ID and dish ID.
     *
     * @param orderId the ID of the order
     * @param dishId  the ID of the dish
     */
    public OrderDishKey(Long orderId, Long dishId) {
        this.orderId = orderId;
        this.dishId = dishId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDishKey that = (OrderDishKey) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(dishId, that.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, dishId);
    }
}