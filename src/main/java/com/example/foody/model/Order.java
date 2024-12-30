package com.example.foody.model;

import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;
import com.example.foody.state.order.OrderStatus;
import com.example.foody.state.order.impl.AwaitingPaymentState;
import com.example.foody.state.order.impl.CompletedState;
import com.example.foody.state.order.impl.PreparingState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "table_code", length = 10, nullable = false)
    private String tableCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderDish> orderDishes = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "buyer_id"))
    })
    private BuyerUser buyer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Transient
    private OrderState state;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(long id, String tableCode, List<OrderDish> orderDishes, BuyerUser buyer, Restaurant restaurant, OrderState state) {
        this.id = id;
        this.tableCode = tableCode;
        this.orderDishes = orderDishes;
        this.buyer = buyer;
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

    public void prepare() {
        state.prepare();
    }

    public void awaitPayment() {
        state.awaitPayment();
    }

    public void complete() {
        state.complete();
    }

    private void setStatus(OrderState state) {
        if (state != null) {
            this.status = OrderStatus.valueOf(state.getName());
        }
    }
}