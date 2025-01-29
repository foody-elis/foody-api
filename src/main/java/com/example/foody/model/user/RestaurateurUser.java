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
import java.util.Optional;

/**
 * Represents a restaurateur user in the system.
 * <p>
 * Extends the {@link User} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.RESTAURATEUR_VALUE)
public class RestaurateurUser extends User {

    @OneToOne(mappedBy = "restaurateur")
    private Restaurant restaurant;

    public RestaurateurUser(
            long id,
            String email,
            String password,
            String name,
            String surname,
            LocalDate birthDate,
            String phoneNumber,
            String avatar,
            Role role,
            boolean active,
            String firebaseCustomToken,
            Restaurant restaurant
    ) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, firebaseCustomToken);
        this.restaurant = restaurant;
    }

    /**
     * Deletes the restaurateur user and its associated restaurant.
     */
    @Override
    public void delete() {
        super.delete();
        Optional.ofNullable(restaurant)
                .ifPresent(Restaurant::delete);
    }
}