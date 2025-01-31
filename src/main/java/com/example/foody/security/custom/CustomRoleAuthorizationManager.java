package com.example.foody.security.custom;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Custom implementation of {@link AuthorizationManager} to handle role-based authorization.
 */
@Component
@AllArgsConstructor
public class CustomRoleAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final Set<Role> requiredRoles;

    /**
     * Checks if the authenticated principal has one of the required roles.
     *
     * @param authentication a supplier of the {@link Authentication} object
     * @param object the {@link RequestAuthorizationContext} object
     * @return an {@link AuthorizationDecision} indicating whether the principal has the required role
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication != null ? authentication.get() : null;
        if (auth == null) {
            return new AuthorizationDecision(false);
        }

        User principal = getUserPrincipal(auth);
        if (principal == null || principal.getRole() == null) {
            return new AuthorizationDecision(false);
        }

        boolean hasRequiredRole = requiredRoles.contains(principal.getRole());
        return new AuthorizationDecision(hasRequiredRole);
    }

    /**
     * Retrieves the {@link User} principal from the {@link Authentication} object.
     *
     * @param authentication the {@link Authentication} object
     * @return the {@link User} principal, or null if not found
     */
    private User getUserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }
}