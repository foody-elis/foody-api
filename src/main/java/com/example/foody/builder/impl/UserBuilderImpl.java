package com.example.foody.builder.impl;

import com.example.foody.builder.UserBuilder;
import com.example.foody.model.user.User;
import com.example.foody.utils.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserBuilderImpl implements UserBuilder {
    private long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String avatar;
    private Role role;
    private boolean active = true;

    @Override
    public UserBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public UserBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @Override
    public UserBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public UserBuilder avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    @Override
    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public UserBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public User build() {
        return new User(
                id,
                email,
                password,
                name,
                surname,
                birthDate,
                phoneNumber,
                avatar,
                role,
                active
        );
    }
}
