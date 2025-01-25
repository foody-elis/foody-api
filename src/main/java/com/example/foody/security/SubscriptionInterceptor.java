package com.example.foody.security;

import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.WebSocketTopics;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionInterceptor implements ChannelInterceptor {

    private final UserService userService;
    private final String[] protectedTopics = {
            WebSocketTopics.TOPIC_ORDERS_PAYED.getName(),
            WebSocketTopics.TOPIC_ORDERS_PREPARING.getName(),
            WebSocketTopics.TOPIC_ORDERS_COMPLETED.getName()
    };

    public SubscriptionInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();

            if (destination != null && accessor.getUser() != null) {
                for (String topic : protectedTopics) {
                    if(destination.matches(topic + "\\d+")) {
                        String restaurantId = destination.substring(topic.length());

                        if (isCookAtRestaurant(accessor.getUser().getName(), Long.parseLong(restaurantId))) return message;

                        break;
                    }
                }
            }

            throw new IllegalArgumentException("No permission for this topic");
        }

        return message;
    }

    private boolean isCookAtRestaurant(String username, long restaurantId) {
        EmployeeUserResponseDTO user = (EmployeeUserResponseDTO) userService.findByEmail(username);
        return user.getEmployerRestaurantId() == restaurantId;
    }
}
