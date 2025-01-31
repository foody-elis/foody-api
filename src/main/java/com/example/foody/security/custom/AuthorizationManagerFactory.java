package com.example.foody.security.custom;

import com.example.foody.utils.enums.Role;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Factory class for creating {@link AuthorizationManager} instances.
 */
@Component
public class AuthorizationManagerFactory {

    /**
     * Creates an {@link AuthorizationManager} for the specified roles.
     *
     * @param roles the roles for which the authorization manager should be created
     * @return an {@link AuthorizationManager} for the specified roles
     */
    public AuthorizationManager<RequestAuthorizationContext> create(Role... roles) {
        return new CustomRoleAuthorizationManager(Set.of(roles));
    }
}