package com.example.foody.security;

import com.example.foody.TestDataUtil;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalWhenAuthorizationHeaderIsMissing() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalWhenAuthorizationHeaderIsInvalid() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalWhenTokenIsValidSetsAuthentication() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        String username = "test@example.com";
        User user = TestDataUtil.createTestCustomerUser();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByEmail(username)).thenReturn(java.util.Optional.of(user));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternalWhenUserNotActiveThrowsException() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        String username = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);

        User user = mock(User.class);
        when(userRepository.findByEmail(username)).thenReturn(java.util.Optional.of(user));
        when(user.isActive()).thenReturn(false);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(handlerExceptionResolver, times(1))
                .resolveException(eq(request), eq(response), isNull(), any(UserNotActiveException.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalWhenUserNotFoundThrowsException() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        String username = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByEmail(username)).thenThrow(new EntityNotFoundException("user", "email", username));

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(handlerExceptionResolver, times(1))
                .resolveException(eq(request), eq(response), isNull(), any(EntityNotFoundException.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}