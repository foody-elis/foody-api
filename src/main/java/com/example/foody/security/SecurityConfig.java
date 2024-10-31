package com.example.foody.security;

import com.example.foody.security.custom.CustomWebSecurity;
import com.example.foody.utils.enums.Role;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
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

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApplicationContext context) throws Exception {
        DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(context);

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(POST, "/api/v1/auth/admins").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/moderators").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/cooks").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(POST, "/api/v1/auth/restaurant/*/waiters").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/auth/user").authenticated()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers(POST, "/api/v1/restaurants").access(getAuthorizationManager(Role.RESTAURATEUR, expressionHandler))
                        .requestMatchers(PATCH, "/api/v1/restaurants/approve/*").hasRole(Role.Constants.MODERATOR_VALUE)
                        .requestMatchers(DELETE, "/api/v1/restaurants/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/restaurants/**").authenticated()

                        .requestMatchers(POST, "/api/v1/categories").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(DELETE,"/api/v1/categories/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/categories/**").authenticated()

                        .requestMatchers(POST, "/api/v1/week-day-infos").access(getAuthorizationManager(Role.RESTAURATEUR, expressionHandler))
                        .requestMatchers(GET, "/api/v1/week-day-infos").hasRole(Role.Constants.ADMIN_VALUE)

                        .requestMatchers(GET, "/api/v1/sitting-times").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/sitting-times/**").authenticated()

                        .requestMatchers(POST, "/api/v1/bookings").access(getAuthorizationManager(Role.CUSTOMER, expressionHandler))
                        .requestMatchers(DELETE, "/api/v1/bookings/*").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers(GET, "/api/v1/bookings/restaurant/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers("/api/v1/bookings/**").authenticated()

                        .requestMatchers(POST, "/api/v1/dishes").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(DELETE, "/api/v1/dishes/*").hasRole(Role.Constants.RESTAURATEUR_VALUE)
                        .requestMatchers(GET, "/api/v1/dishes").hasRole(Role.Constants.ADMIN_VALUE)
                        .requestMatchers("/api/v1/dishes/**").authenticated()

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

    private WebExpressionAuthorizationManager getAuthorizationManager(Role role, DefaultHttpSecurityExpressionHandler expressionHandler) {
        WebExpressionAuthorizationManager authorizationManager =
                new WebExpressionAuthorizationManager("@customWebSecurity.hasSpecificRole(authentication, '" + role + "')");
        authorizationManager.setExpressionHandler(expressionHandler);
        return authorizationManager;
    }
}
