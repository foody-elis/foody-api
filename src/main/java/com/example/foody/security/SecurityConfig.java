package com.example.foody.security;

import com.example.foody.utils.Role;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
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

    // todo test
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(POST, "/api/v1/auth/register-moderator").hasRole(Role.ADMIN.name())
                        .requestMatchers(POST, "/api/v1/auth/register-restaurateur").hasRole(Role.MODERATOR.name())
                        .requestMatchers(POST, "/api/v1/auth/register-cook").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers(POST, "/api/v1/auth/register-waiter").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers(GET, "/api/v1/auth/logged-user").authenticated() // todo remove
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers(POST, "/api/v1/restaurants").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers(PATCH, "/api/v1/restaurants/approve/*").hasRole(Role.MODERATOR.name())
                        .requestMatchers(DELETE, "/api/v1/restaurants/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/v1/restaurants/**").authenticated()

                        .requestMatchers(POST, "/api/v1/categories").hasRole(Role.ADMIN.name())
                        .requestMatchers(DELETE,"/api/v1/categories/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/v1/categories/**").authenticated()

                        .requestMatchers(POST, "/api/v1/sitting-times").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers(DELETE, "/api/v1/sitting-times/**").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers(GET, "/api/v1/sitting-times").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/v1/sitting-times/**").authenticated()

                        .requestMatchers(POST, "/api/v1/bookings").hasRole(Role.CUSTOMER.name())
                        .requestMatchers(GET, "/api/v1/bookings").hasRole(Role.ADMIN.name())
                        .requestMatchers(GET, "/api/v1/bookings/restaurant/*").authenticated()

                        .requestMatchers(POST, "/api/v1/bookings").authenticated()
                        .requestMatchers(DELETE, "/api/v1/bookings/*").hasRole(Role.ADMIN.name())
                        .requestMatchers(GET, "/api/v1/bookings").hasRole(Role.ADMIN.name())
                        .requestMatchers(GET, "/api/v1/bookings/restaurant/*").hasRole(Role.RESTAURATEUR.name())
                        .requestMatchers("/api/v1/bookings/**").authenticated()

                        .anyRequest().permitAll() // todo remove?
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                // exception handling: AuthenticationException, AccessDeniedException
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
                        .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
