package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import com.example.foody.state.order.*;
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
    @JoinColumn(name = "customer_id")
    private CustomerUser customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Transient
    private OrderState state;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(long id, String tableNumber, List<Dish> dishes, CustomerUser customer, Restaurant restaurant, OrderState state) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.dishes = dishes;
        this.customer = customer;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
    }

    public OrderState getState() {
        if (state == null && status != null) {
            switch (status) {
                case OrderStatus.PREPARING -> state = new PreparingState(this);
                case OrderStatus.AWAITING_PAYMENT -> state = new AwaitingPaymentState(this);
                case OrderStatus.COMPLETED -> state = new CompletedState(this);
            }
        }
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
        setStatus(state);
    }

    // todo make private?
    private void setStatus(OrderState state) {
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
