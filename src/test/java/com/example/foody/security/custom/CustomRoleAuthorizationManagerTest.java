package com.example.foody.security.custom;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomRoleAuthorizationManagerTest {

    private CustomRoleAuthorizationManager authorizationManager;

    private Set<Role> requiredRoles;

    @BeforeEach
    void setUp() {
        requiredRoles = Set.of(Role.ADMIN, Role.MODERATOR);
        authorizationManager = new CustomRoleAuthorizationManager(requiredRoles);
    }

    @Test
    void checkWhenAuthenticationIsNullReturnsFalse() {
        // Arrange
        Supplier<Authentication> authenticationSupplier = () -> null;
        RequestAuthorizationContext context = mock(RequestAuthorizationContext.class);

        // Act
        AuthorizationDecision decision = authorizationManager.check(authenticationSupplier, context);

        // Assert
        assertFalse(decision.isGranted());
    }

    @Test
    void checkWhenPrincipalIsNotUserReturnsFalse() {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn("NotAUser");

        Supplier<Authentication> authenticationSupplier = () -> authentication;
        RequestAuthorizationContext context = mock(RequestAuthorizationContext.class);

        // Act
        AuthorizationDecision decision = authorizationManager.check(authenticationSupplier, context);

        // Assert
        assertFalse(decision.isGranted());
    }

    @Test
    void checkWhenPrincipalHasNoRoleReturnsFalse() {
        // Arrange
        User user = mock(User.class);
        when(user.getRole()).thenReturn(null);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        Supplier<Authentication> authenticationSupplier = () -> authentication;
        RequestAuthorizationContext context = mock(RequestAuthorizationContext.class);

        // Act
        AuthorizationDecision decision = authorizationManager.check(authenticationSupplier, context);

        // Assert
        assertFalse(decision.isGranted());
    }

    @Test
    void checkWhenPrincipalHasRequiredRoleReturnsTrue() {
        // Arrange
        User user = mock(User.class);
        when(user.getRole()).thenReturn(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        Supplier<Authentication> authenticationSupplier = () -> authentication;
        RequestAuthorizationContext context = mock(RequestAuthorizationContext.class);

        // Act
        AuthorizationDecision decision = authorizationManager.check(authenticationSupplier, context);

        // Assert
        assertTrue(decision.isGranted());
    }

    @Test
    void checkWhenPrincipalDoesNotHaveRequiredRoleReturnsFalse() {
        // Arrange
        User user = mock(User.class);
        when(user.getRole()).thenReturn(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        Supplier<Authentication> authenticationSupplier = () -> authentication;
        RequestAuthorizationContext context = mock(RequestAuthorizationContext.class);

        // Act
        AuthorizationDecision decision = authorizationManager.check(authenticationSupplier, context);

        // Assert
        assertFalse(decision.isGranted());
    }
}