package com.example.foody.model.user;

import com.example.foody.model.CreditCard;
import com.example.foody.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.ADMIN_VALUE)
public class AdminUser extends User {
    public AdminUser() {
    }

    public AdminUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, CreditCard creditCard) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, creditCard);
    }
}
