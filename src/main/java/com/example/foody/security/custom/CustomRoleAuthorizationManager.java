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
        if (authentication == null || authentication.get() == null) {
            return new AuthorizationDecision(false);
        }

        User principal = (User) authentication.get().getPrincipal();
        if (principal == null || principal.getRole() == null) {
            return new AuthorizationDecision(false);
        }

        boolean hasRole = requiredRoles.contains(principal.getRole());
        return new AuthorizationDecision(hasRole);
    }
}