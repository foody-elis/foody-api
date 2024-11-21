package com.example.foody.security.custom;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

@Component
public class CustomRoleAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final Set<Role> requiredRoles;

    public CustomRoleAuthorizationManager(Set<Role> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    // This method checks if the principal has a specific role, it does not consider the role hierarchy
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

    private User getUserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }
}