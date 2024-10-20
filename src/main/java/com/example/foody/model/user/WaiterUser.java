package com.example.foody.model.user;

import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.WAITER_VALUE)
public class WaiterUser extends User {
    @ManyToOne
    @JoinColumn(name = "employer_restaurant_id")
    private Restaurant employerRestaurant;

    public WaiterUser() {
    }

    public WaiterUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, CreditCard creditCard, Restaurant employerRestaurant) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, creditCard);
        this.employerRestaurant = employerRestaurant;
    }
}
