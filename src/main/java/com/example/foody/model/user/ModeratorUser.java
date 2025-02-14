package com.example.foody.model.user;

import com.example.foody.utils.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a moderator user in the system.
 * <p>
 * Extends the {@link User} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.MODERATOR_VALUE)
public class ModeratorUser extends User {

    public ModeratorUser(
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
            String firebaseCustomToken
    ) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, firebaseCustomToken);
    }
}