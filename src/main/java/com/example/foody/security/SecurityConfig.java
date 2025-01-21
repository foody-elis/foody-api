package com.example.foody.security;

import com.example.foody.security.custom.AuthorizationManagerFactory;
import com.example.foody.utils.enums.Role;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthorizationManagerFactory authorizationManagerFactory;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, HandlerExceptionResolver handlerExceptionResolver, AuthorizationManagerFactory authorizationManagerFactory) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authorizationManagerFactory = authorizationManagerFactory;
    }

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

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(POST, "/api/v1/auth/admins").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/moderators").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/cooks").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/waiters").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(PATCH, "/api/v1/auth/change-password").authenticated()
                        .requestMatchers(GET, "/api/v1/auth/user").authenticated()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers(POST, "/api/v1/restaurants").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(PUT, "/api/v1/restaurants/*").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(DELETE, "/api/v1/restaurants/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(PATCH, "/api/v1/restaurants/approve/*").hasRole(Role.Constants.MODERATOR_VALUE)
                        .requestMatchers(GET, "/api/v1/restaurants/restaurateur").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers("/api/v1/restaurants/**").authenticated()

                        .requestMatchers(POST, "/api/v1/categories").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(DELETE,"/api/v1/categories/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/categories/**").authenticated()

                        .requestMatchers(POST, "/api/v1/week-day-infos").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(PUT, "/api/v1/week-day-infos/*").access(hasSpecificRole(Role.RESTAURATEUR))
                        .requestMatchers(GET, "/api/v1/week-day-infos").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/week-day-infos/*").authenticated()

                        .requestMatchers(GET, "/api/v1/sitting-times").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/sitting-times/**").authenticated()

                        .requestMatchers(POST, "/api/v1/bookings").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(DELETE, "/api/v1/bookings/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(PATCH, "/api/v1/bookings/cancel/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings/customer").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(GET, "/api/v1/bookings/restaurant/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers("/api/v1/bookings/**").authenticated()

                        .requestMatchers(POST, "/api/v1/dishes").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(PUT, "/api/v1/dishes/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(DELETE, "/api/v1/dishes/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/dishes").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/dishes/**").authenticated()

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

                        .requestMatchers(POST, "/api/v1/reviews").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers(DELETE, "/api/v1/reviews/*").hasAnyRole(Role.Constants.MODERATOR_VALUE, Role.Constants.CUSTOMER_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews/*").hasAnyRole(Role.Constants.CUSTOMER_VALUE, Role.Constants.COOK_VALUE, Role.Constants.WAITER_VALUE)
                        .requestMatchers(GET, "/api/v1/reviews/customer").access(hasSpecificRole(Role.CUSTOMER))
                        .requestMatchers("/api/v1/reviews/**").authenticated()

                        .requestMatchers(PUT, "/api/v1/users").authenticated()
                        .requestMatchers(PATCH, "/api/v1/users/chat-id").authenticated()
                        .requestMatchers(GET, "/api/v1/users/*").authenticated()
                        .requestMatchers("/api/v1/users/**").hasRole(Role.Constants.ADMIN_VALUE)

                        .anyRequest().permitAll() // todo remove?
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

    private AuthorizationManager<RequestAuthorizationContext> hasSpecificRole(Role... roles) {
        return authorizationManagerFactory.create(roles);
    }
}