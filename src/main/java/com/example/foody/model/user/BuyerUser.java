package com.example.foody.model.user;

import com.example.foody.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Embeddable
public class BuyerUser {
    private long id;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public BuyerUser() {
    }

    public BuyerUser(long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}
