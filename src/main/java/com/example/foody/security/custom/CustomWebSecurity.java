package com.example.foody.security.custom;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomWebSecurity {
    // Method to check if the user has a specific role, it does not consider the role hierarchy
    public boolean hasSpecificRole(Authentication authentication, Role role) {
        User principal = (User) authentication.getPrincipal();
        return principal.getRole().equals(role);
    }
}