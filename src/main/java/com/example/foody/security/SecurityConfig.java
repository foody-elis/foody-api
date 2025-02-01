package com.example.foody.security;

import com.example.foody.security.custom.AuthorizationManagerFactory;
import com.example.foody.utils.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration class for the application.
 * <p>
 * This class configures the security settings, including role hierarchy, method security, and HTTP security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthorizationManagerFactory authorizationManagerFactory;

    /**
     * Bean definition for role hierarchy.
     *
     * @return the role hierarchy
     */
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                """
                        ROLE_ADMIN > ROLE_CUSTOMER
                        ROLE_ADMIN > ROLE_MODERATOR
                        ROLE_MODERATOR > ROLE_RESTAURATEUR
                        ROLE_RESTAURATEUR > ROLE_COOK
                        ROLE_RESTAURATEUR > ROLE_WAITER
                        """
        );
    }

    /**
     * Bean definition for method security expression handler.
     *
     * @param roleHierarchy the role hierarchy
     * @return the method security expression handler
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    /**
     * Bean definition for security filter chain.
     *
     * @param http the HTTP security
     * @return the security filter chain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // Authentication endpoints
                        .requestMatchers(POST, "/api/v1/auth/admins").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/moderators").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/cooks").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/waiters").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(PATCH, "/api/v1/auth/change-password").authenticated()
                        .requestMatchers(GET, "/api/v1/auth/user").authenticated()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Restaurant endpoints
                        .requestMatchers(POST, "/api/v1/restaurants").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(PUT, "/api/v1/restaurants/*").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(DELETE, "/api/v1/restaurants/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(PATCH, "/api/v1/restaurants/approve/*").hasRole(Role.Constants.MODERATOR_VALUE)
                        .requestMatchers(GET, "/api/v1/restaurants/restaurateur").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers("/api/v1/restaurants/**").authenticated()

                        // Category endpoints
                        .requestMatchers(POST, "/api/v1/categories").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(DELETE, "/api/v1/categories/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/categories/**").authenticated()

                        // WeekDayInfo endpoints
                        .requestMatchers(POST, "/api/v1/week-day-infos").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(PUT, "/api/v1/week-day-infos/*").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(GET, "/api/v1/week-day-infos").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/week-day-infos/*").authenticated()

                        // SittingTime endpoints
                        .requestMatchers(GET, "/api/v1/sitting-times").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/sitting-times/**").authenticated()

                        // Booking endpoints
                        .requestMatchers(POST, "/api/v1/bookings").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(DELETE, "/api/v1/bookings/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(PATCH, "/api/v1/bookings/cancel/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings/customer").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(GET, "/api/v1/bookings/customer/current").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(GET, "/api/v1/bookings/restaurant/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers("/api/v1/bookings/**").authenticated()

                        // Dish endpoints
                        .requestMatchers(POST, "/api/v1/dishes").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(PUT, "/api/v1/dishes/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(DELETE, "/api/v1/dishes/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/dishes").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/dishes/**").authenticated()

                        // Order endpoints
                        .requestMatchers(POST, "/api/v1/orders").access(hasSpecificRole(Role.CUSTOMER, Role.WAITER))
                        .requestMatchers(DELETE, "/api/v1/orders").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(PATCH, "/api/v1/orders/pay/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.WAITER_VALUE)
                        .requestMatchers(PATCH, "/api/v1/orders/prepare/*").hasRole(Role.Constants.COOK_VALUE)
                        .requestMatchers(PATCH, "/api/v1/orders/complete/*").hasRole(Role.Constants.COOK_VALUE)
                        .requestMatchers(GET, "/api/v1/orders").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/orders/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.WAITER_VALUE)
                        .requestMatchers(GET, "/api/v1/orders/buyer").access(hasSpecificRole(Role.CUSTOMER, Role.WAITER))
                        .requestMatchers(GET, "/api/v1/orders/restaurant/*").hasAnyRole(Role.Constants.RESTAURATEUR_VALUE, Role.Constants.COOK_VALUE, Role.Constants.WAITER_VALUE)
                        .requestMatchers(GET, "/api/v1/orders/restaurant/*/in-progress").hasAnyRole(Role.Constants.RESTAURATEUR_VALUE, Role.Constants.COOK_VALUE)
                        .requestMatchers("/api/v1/orders/**").authenticated()

                        // Review endpoints
                        .requestMatchers(POST, "/api/v1/reviews").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(DELETE, "/api/v1/reviews/*").hasAnyRole(Role.Constants.MODERATOR_VALUE, Role.Constants.CUSTOMER_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.COOK_VALUE, Role.Constants.WAITER_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews/customer").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers("/api/v1/reviews/**").authenticated()

                        // User endpoints
                        .requestMatchers(PUT, "/api/v1/users").authenticated()
                        .requestMatchers(PATCH, "/api/v1/users/chat-id").authenticated()
                        .requestMatchers(GET, "/api/v1/users/*").authenticated()
                        .requestMatchers("/api/v1/users/**").hasRole(Role.Constants.ADMIN_VALUE)

                        // WebSocket endpoints
                        .requestMatchers("/ws").hasRole(Role.Constants.COOK_VALUE)

                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                // Exception handling: AuthenticationException, AccessDeniedException
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
                        .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates an authorization manager for specific roles.
     *
     * @param roles the roles
     * @return the authorization manager
     */
    private AuthorizationManager<RequestAuthorizationContext> hasSpecificRole(Role... roles) {
        return authorizationManagerFactory.create(roles);
    }
}