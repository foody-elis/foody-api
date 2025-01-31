package com.example.foody.observer.listener;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.CookUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.CookUserOrderCreatedEventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link CookUserOrderCreatedEventListener} class using mock services.
 */
public class CookUserOrderCreatedEventListenerTest {

    private EmailService emailService;
    private CookUser cookUser;
    private CookUserOrderCreatedEventListener listener;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        cookUser = mock(CookUser.class);
        listener = new CookUserOrderCreatedEventListener(emailService, cookUser);
    }

    @Test
    void updateWhenCalledSendsEmailWithCorrectDetails() {
        // Arrange
        Order order = mock(Order.class);
        Restaurant restaurant = mock(Restaurant.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User buyerUser = mock(User.class);

        when(order.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getName()).thenReturn("Test Restaurant");
        when(order.getId()).thenReturn(123L);

        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getUser()).thenReturn(buyerUser);
        when(buyerUser.getName()).thenReturn("John");
        when(buyerUser.getSurname()).thenReturn("Doe");

        when(cookUser.getName()).thenReturn("CookName");
        when(cookUser.getSurname()).thenReturn("CookSurname");
        when(cookUser.getEmail()).thenReturn("cook@example.com");

        Map<EmailPlaceholder, Object> expectedVariables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, "Test Restaurant",
                EmailPlaceholder.COOK_NAME, "CookName",
                EmailPlaceholder.COOK_SURNAME, "CookSurname",
                EmailPlaceholder.ORDER_ID, 123L,
                EmailPlaceholder.CUSTOMER_NAME, "John",
                EmailPlaceholder.CUSTOMER_SURNAME, "Doe"
        );

        // Act
        listener.update(order);

        // Assert
        verify(emailService, times(1)).sendTemplatedEmail(
                "cook@example.com",
                EmailTemplateType.NEW_ORDER,
                expectedVariables
        );
    }
}