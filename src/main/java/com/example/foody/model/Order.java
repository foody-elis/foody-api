package com.example.foody.model;

import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;
import com.example.foody.utils.state.OrderStateUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order entity in the system.
 * <p>
 * Extends {@link DefaultEntity}.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@SQLRestriction("deleted_at IS NULL")
public class Order extends DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "table_code", length = 10, nullable = false)
    private String tableCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderDish> orderDishes = new ArrayList<>();

    /**
     * The buyer associated with the order.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "buyer_id"))
    })
    private BuyerUser buyer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /**
     * The current state of the order.
     */
    @Transient
    private OrderState state;

    /**
     * The status of the order.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(
            long id,
            String tableCode,
            List<OrderDish> orderDishes,
            BuyerUser buyer,
            Restaurant restaurant,
            OrderState state
    ) {
        this.id = id;
        this.tableCode = tableCode;
        this.orderDishes = orderDishes;
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
    }

    /**
     * Gets the current state of the order.
     *
     * @return the current state of the order
     */
    public OrderState getState() {
        if (state == null && status != null) {
            state = OrderStateUtils.getState(status);
        }
        return state;
    }

    /**
     * Sets the state of the order.
     *
     * @param state the new state of the order
     */
    public void setState(OrderState state) {
        this.state = state;
        setStatus(state);
    }

    /**
     * Creates the order by invoking the create method on the current state.
     */
    public void create() {
        state.create(this);
    }

    /**
     * Pays for the order by invoking the pay method on the current state.
     */
    public void pay() {
        state.pay(this);
    }

    /**
     * Prepares the order by invoking the prepare method on the current state.
     */
    public void prepare() {
        state.prepare(this);
    }

    /**
     * Completes the order by invoking the complete method on the current state.
     */
    public void complete() {
        state.complete(this);
    }

    /**
     * Sets the status of the order based on the current state.
     *
     * @param state the current state of the order
     */
    private void setStatus(OrderState state) {
        this.status = state != null ? state.getStatus() : null;
    }
}