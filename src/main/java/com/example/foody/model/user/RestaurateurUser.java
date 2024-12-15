package com.example.foody.model.user;

import com.example.foody.model.Restaurant;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.RESTAURATEUR_VALUE)
public class RestaurateurUser extends User {
    @OneToOne(mappedBy = "restaurateur")
    private Restaurant restaurant;

    public RestaurateurUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, Restaurant restaurant) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active);
        this.restaurant = restaurant;
    }

    @Override
    public void delete() {
        super.delete();
        restaurant.delete();
    }
}