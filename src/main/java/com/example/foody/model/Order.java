package com.example.foody.model;

import com.example.foody.state.order.OrderState;
import com.example.foody.state.order.OrderStatus;
import com.example.foody.state.order.PreparingState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "table_number", length = 10, nullable = false)
    private String tableNumber;

    @ManyToMany
    @JoinTable(
            name = "order_dish",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Transient
    private OrderState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    public Order() {
    }

    public Order(long id, String tableNumber, List<Dish> dishes, User user, Restaurant restaurant, OrderState state) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.dishes = dishes;
        this.user = user;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
    }

    public void setState(OrderState state) {
        this.state = state;
        setStatus(state);
    }

    public void setStatus(OrderState state) {
        if (state != null) {
            this.status = OrderStatus.valueOf(state.getName());
        }
    }

    public void prepare() {
        state.prepare();
    }

    public void awaitPayment() {
        state.awaitPayment();
    }

    public void complete() {
        state.complete();
    }
}
