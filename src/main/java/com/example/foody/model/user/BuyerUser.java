package com.example.foody.model.user;

import com.example.foody.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a buyer user in the system.
 * <p>
 * This class is embeddable and can be used as a component in other entities.
 */
@Data
@NoArgsConstructor
@Embeddable
public class BuyerUser {

    private Long id;

    /**
     * The user associated with the buyer.
     * <p>
     * This field is transient and not persisted in the database.
     */
    @Transient
    private User user;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public BuyerUser(Long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}