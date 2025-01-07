package com.example.foody.builder;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;

import java.time.LocalDate;

public interface UserBuilder<U extends User> {
    UserBuilder<U> id(long id);
    UserBuilder<U> email(String email);
    UserBuilder<U> password(String password);
    UserBuilder<U> name(String name);
    UserBuilder<U> surname(String surname);
    UserBuilder<U> birthDate(LocalDate birthDate);
    UserBuilder<U> phoneNumber(String phoneNumber);
    UserBuilder<U> avatarUrl(String avatarUrl);
    UserBuilder<U> role(Role role);
    UserBuilder<U> active(boolean active);
    UserBuilder<U> chatId(Long chatId);
    U build();
}