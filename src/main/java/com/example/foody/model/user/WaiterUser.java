package com.example.foody.model.user;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.WAITER_VALUE)
public class WaiterUser extends EmployeeUser {
//    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Order> orders = new ArrayList<>();

    @Embedded
    private BuyerUser buyer;

    public WaiterUser() {
    }

    public WaiterUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, Restaurant employerRestaurant, List<Order> orders) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, employerRestaurant);
    }
}
