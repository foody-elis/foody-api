package com.example.foody.builder;

import com.example.foody.model.user.User;
import com.example.foody.utils.Role;

import java.time.LocalDate;

public interface UserBuilder {
    UserBuilder id(long id);
    UserBuilder email(String email);
    UserBuilder password(String password);
    UserBuilder name(String name);
    UserBuilder surname(String surname);
    UserBuilder birthDate(LocalDate birthDate);
    UserBuilder phoneNumber(String phoneNumber);
    UserBuilder avatar(String avatar);
    UserBuilder role(Role role);
    UserBuilder active(boolean active);
    User build();
}
