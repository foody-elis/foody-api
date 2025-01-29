package com.example.foody.security;

import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.WebSocketTopics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionInterceptorTest {

    private SubscriptionInterceptor interceptor;

    @Mock
    private UserService userService;

    @Mock
    private MessageChannel messageChannel;

    @BeforeEach
    void setUp() {
        interceptor = new SubscriptionInterceptor(userService);
    }

    @Test
    void preSendWhenSubscriptionToAllowedTopicReturnsMessage() {
        // Arrange
        String restaurantId = "123";
        String topic = WebSocketTopics.TOPIC_ORDERS_PAYED.getName() + restaurantId;
        EmployeeUserResponseDTO mockUser = new EmployeeUserResponseDTO();
        mockUser.setEmployerRestaurantId(Long.parseLong(restaurantId));

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination(topic);
        accessor.setUser(() -> "user@example.com");

        Message<?> message = new GenericMessage<>(new byte[0], accessor.getMessageHeaders());

        when(userService.findByEmail("user@example.com")).thenReturn(mockUser);

        // Act
        Message<?> result = interceptor.preSend(message, messageChannel);

        // Assert
        assertNotNull(result);
        assertEquals(message, result);
        verify(userService, times(1)).findByEmail("user@example.com");
    }

    @Test
    void preSendWhenSubscriptionToProtectedTopicWithoutPermissionThrowsException() {
        // Arrange
        String restaurantId = "123";
        String topic = WebSocketTopics.TOPIC_ORDERS_PREPARING.getName() + restaurantId;
        EmployeeUserResponseDTO mockUser = new EmployeeUserResponseDTO();
        mockUser.setEmployerRestaurantId(999L);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination(topic);
        accessor.setUser(() -> "user@example.com");

        Message<?> message = new GenericMessage<>(new byte[0], accessor.getMessageHeaders());

        when(userService.findByEmail("user@example.com")).thenReturn(mockUser);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> interceptor.preSend(message, messageChannel));
        verify(userService, times(1)).findByEmail("user@example.com");
    }

    @Test
    void preSendWhenSubscriptionWithoutUserThrowsException() {
        // Arrange
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination(WebSocketTopics.TOPIC_ORDERS_COMPLETED.getName() + "123");

        Message<?> message = new GenericMessage<>(new byte[0], accessor.getMessageHeaders());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> interceptor.preSend(message, messageChannel));
        verifyNoInteractions(userService);
    }

    @Test
    void preSendWhenNonSubscriptionCommandReturnsMessage() {
        // Arrange
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SEND);
        Message<?> message = new GenericMessage<>(new byte[0], accessor.getMessageHeaders());

        // Act
        Message<?> result = interceptor.preSend(message, messageChannel);

        // Assert
        assertNotNull(result);
        assertEquals(message, result);
        verifyNoInteractions(userService);
    }
}