package com.example.foody.security.custom;

import com.example.foody.utils.enums.Role;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuthorizationManagerFactory {

    public AuthorizationManager<RequestAuthorizationContext> create(Role... roles) {
        return new CustomRoleAuthorizationManager(Set.of(roles));
    }
}
