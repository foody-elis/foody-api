package com.example.foody.model.user;

import com.example.foody.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Embeddable
public class BuyerUser {
    private Long id;

    // Field user is set when an order is retrieved from the database
    @Transient
    private User user;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public BuyerUser(Long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}