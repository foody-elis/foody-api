package com.example.foody.builder.impl;

import com.example.foody.builder.ModeratorUserBuilder;
import com.example.foody.model.user.ModeratorUser;
import org.springframework.stereotype.Component;

@Component
public class ModeratorUserBuilderImpl extends UserBuilderImpl<ModeratorUser> implements ModeratorUserBuilder {
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