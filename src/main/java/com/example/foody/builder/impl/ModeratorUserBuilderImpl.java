package com.example.foody.builder.impl;

import com.example.foody.builder.ModeratorUserBuilder;
import com.example.foody.model.user.ModeratorUser;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link ModeratorUserBuilder} interface.
 */
@Component
public class ModeratorUserBuilderImpl extends UserBuilderImpl<ModeratorUser> implements ModeratorUserBuilder {

    /**
     * Builds and returns the {@link ModeratorUser} object.
     *
     * @return the built {@link ModeratorUser} object
     */
    @Override
    public ModeratorUser build() {
        return new ModeratorUser(
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