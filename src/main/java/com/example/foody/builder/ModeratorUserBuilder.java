package com.example.foody.builder;

import com.example.foody.model.user.ModeratorUser;

/**
 * Interface for building {@link ModeratorUser} objects.
 * Extends the {@link UserBuilder} interface with ModeratorUser as the type parameter.
 */
public interface ModeratorUserBuilder extends UserBuilder<ModeratorUser> {
}