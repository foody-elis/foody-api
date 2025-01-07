package com.example.foody.builder.impl;

import com.example.foody.builder.AdminUserBuilder;
import com.example.foody.model.user.AdminUser;
import org.springframework.stereotype.Component;

@Component
public class AdminUserBuilderImpl extends UserBuilderImpl<AdminUser> implements AdminUserBuilder {
    @Override
    public AdminUser build() {
        return new AdminUser(
                id,
                email,
                password,
                name,
                surname,
                birthDate,
                phoneNumber,
                avatarUrl,
                role,
                active,
                chatId
        );
    }
}