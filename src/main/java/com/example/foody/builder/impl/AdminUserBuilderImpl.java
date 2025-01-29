package com.example.foody.builder.impl;

import com.example.foody.builder.AdminUserBuilder;
import com.example.foody.model.user.AdminUser;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link AdminUserBuilder} interface.
 */
@Component
public class AdminUserBuilderImpl extends UserBuilderImpl<AdminUser> implements AdminUserBuilder {
    /**
     * Builds and returns the {@link AdminUser} object.
     *
     * @return the built {@link AdminUser} object
     */
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
                firebaseCustomToken
        );
    }
}