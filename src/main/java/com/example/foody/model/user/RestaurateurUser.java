package com.example.foody.model.user;

import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.RESTAURATEUR_VALUE)
public class RestaurateurUser extends User {
    @OneToOne(mappedBy = "restaurateur")
    private Restaurant ownsRestaurant;

    public RestaurateurUser() {
    }

    public RestaurateurUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, CreditCard creditCard, Restaurant ownsRestaurant) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, creditCard);
        this.ownsRestaurant = ownsRestaurant;
    }
}
