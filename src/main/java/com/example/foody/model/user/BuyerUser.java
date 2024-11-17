package com.example.foody.model.user;

import com.example.foody.model.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Embeddable
public class BuyerUser {
    private Long id;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public BuyerUser() {
    }

    public BuyerUser(long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}