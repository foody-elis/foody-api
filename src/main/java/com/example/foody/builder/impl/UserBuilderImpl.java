package com.example.foody.builder.impl;

import com.example.foody.builder.UserBuilder;
import com.example.foody.model.user.User;
import com.example.foody.utils.Role;

import java.time.LocalDate;

public abstract class UserBuilderImpl<U extends User> implements UserBuilder<U> {
    protected long id;
    protected String email;
    protected String password;
    protected String name;
    protected String surname;
    protected LocalDate birthDate;
    protected String phoneNumber;
    protected String avatar;
    protected Role role;
    protected boolean active = true;

    @Override
    public UserBuilder<U> id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public UserBuilder<U> email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilder<U> password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilder<U> name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserBuilder<U> surname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public UserBuilder<U> birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    @Override
    public UserBuilder<U> phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public UserBuilder<U> avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    @Override
    public UserBuilder<U> role(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public UserBuilder<U> active(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public abstract U build();
}
