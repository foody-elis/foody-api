package com.example.foody.security;

import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.WebSocketTopics;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Interceptor for handling WebSocket subscription requests.
 * <p>
 * Ensures that only authorized users can subscribe to protected topics.
 */
@Component
@AllArgsConstructor
public class SubscriptionInterceptor implements ChannelInterceptor {

    private final UserService userService;
    private final String[] protectedTopics = {
            WebSocketTopics.TOPIC_ORDERS_PAYED.getName(),
            WebSocketTopics.TOPIC_ORDERS_PREPARING.getName(),
            WebSocketTopics.TOPIC_ORDERS_COMPLETED.getName()
    };

    /**
     * Intercepts the message before it is sent to the channel.
     * Checks if the user has permission to subscribe to the requested topic.
     *
     * @param message the message to be sent
     * @param channel the message channel
     * @return the message if the user has permission, otherwise throws an exception
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Access the STOMP headers from the message
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Check if the command is a subscription request
        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();

            if (destination != null && accessor.getUser() != null) {
                // Iterate over the protected topics to check if the user has access
                for (String topic : protectedTopics) {
                    if (destination.matches(topic + "\\d+")) {
                        String restaurantId = destination.substring(topic.length());

                        // Check if the user is a cook at the restaurant
                        if (isCookAtRestaurant(accessor.getUser().getName(), Long.parseLong(restaurantId)))
                            return message;

                        break;
                    }
                }
            }

            throw new IllegalArgumentException("No permission for this topic");
        }

        return message;
    }

    /**
     * Checks if the user is a cook at the specified restaurant.
     *
     * @param username     the username of the user
     * @param restaurantId the ID of the restaurant
     * @return true if the user is a cook at the restaurant, false otherwise
     */
    private boolean isCookAtRestaurant(String username, long restaurantId) {
        EmployeeUserResponseDTO user = (EmployeeUserResponseDTO) userService.findByEmail(username);
        return user.getEmployerRestaurantId() == restaurantId;
    }
}