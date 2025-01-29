package com.example.foody.model.order_dish;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the association between an order and a dish in the system.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "order_dish")
public class OrderDish {

    /**
     * The composite key for the OrderDish entity.
     */
    @EmbeddedId
    private OrderDishKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    /**
     * Constructs an OrderDish with the specified order, dish, and quantity.
     *
     * @param order    the order associated with this OrderDish
     * @param dish     the dish associated with this OrderDish
     * @param quantity the quantity of the dish in the order
     */
    public OrderDish(Order order, Dish dish, int quantity) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.id = new OrderDishKey(order.getId(), dish.getId());
    }
}