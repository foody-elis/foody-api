package com.example.foody.model.order_dish;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_dish")
public class OrderDish {
    @EmbeddedId
    private OrderDishKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public OrderDish() {
    }

    public OrderDish(Order order, Dish dish, int quantity) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.id = new OrderDishKey(order.getId(), dish.getId());
    }
}