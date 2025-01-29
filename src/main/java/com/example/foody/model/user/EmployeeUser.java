package com.example.foody.model.user;

import com.example.foody.model.Restaurant;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents an employee user in the system.
 * <p>
 * Extends the {@link User} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public abstract class EmployeeUser extends User {

    @ManyToOne
    @JoinColumn(name = "employer_restaurant_id")
    private Restaurant employerRestaurant;

    public EmployeeUser(
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
            Restaurant employerRestaurant
    ) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, firebaseCustomToken);
        this.employerRestaurant = employerRestaurant;
    }
}