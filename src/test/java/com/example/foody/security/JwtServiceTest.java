package com.example.foody.security;

import com.example.foody.TestDataUtil;
import com.example.foody.model.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    @Mock
    private UserDetails userDetails;

    @Test
    void generateTokenWhenValidUserReturnsToken() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        setTestJwtProperties();

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // Typical start for a JWT
    }

    @Test
    void extractUsernameWhenValidTokenReturnsUsername() {
        // Arrange
        setTestJwtProperties();
        User user = TestDataUtil.createTestCustomerUser();
        String token = jwtService.generateToken(user);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals(user.getEmail(), username);
    }

    @Test
    void extractExpirationWhenValidTokenReturnsExpirationDate() {
        // Arrange
        setTestJwtProperties();
        User user = TestDataUtil.createTestCustomerUser();
        String token = jwtService.generateToken(user);

        // Act
        Date expirationDate = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void validateTokenWhenValidTokenAndUserReturnsTrue() {
        // Arrange
        setTestJwtProperties();
        User user = TestDataUtil.createTestCustomerUser();
        String token = jwtService.generateToken(user);

        when(userDetails.getUsername()).thenReturn(user.getEmail());

        // Act
        Boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateTokenWhenInvalidTokenReturnsFalse() {
        // Arrange
        setTestJwtProperties();
        User user = TestDataUtil.createTestCustomerUser();
        String token = jwtService.generateToken(user);

        when(userDetails.getUsername()).thenReturn("wrong-email@example.com");

        // Act
        Boolean isValid = jwtService.validateToken(token, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateTokenWhenExpiredTokenThrowsExpiredJwtException() {
        // Arrange
        setTestJwtProperties();
        User user = TestDataUtil.createTestCustomerUser();
        String expiredToken = generateExpiredToken(user);

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> jwtService.validateToken(expiredToken, userDetails));
    }

    private void setTestJwtProperties() {
        jwtService.setSECRET("dGVzdHNlY3JldHZhbHVlMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkw");
        jwtService.setEXPIRATION(60000); // 60 secondi
    }

    private String generateExpiredToken(User user) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000)) // Token issued 2 seconds ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Token expired 1 second ago
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtService.getSECRET())))
                .compact();
    }
}