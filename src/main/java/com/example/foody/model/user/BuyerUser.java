package com.example.foody.model.user;

import com.example.foody.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public abstract class BuyerUser {
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}
