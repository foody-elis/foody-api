package com.example.foody.config;

import com.example.foody.security.SubscriptionInterceptor;
import com.example.foody.utils.enums.WebSocketTopics;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for setting up WebSocket message broker.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SubscriptionInterceptor subscriptionInterceptor;

    /**
     * Constructor to initialize WebSocketConfig with a SubscriptionInterceptor.
     *
     * @param subscriptionInterceptor the interceptor to handle subscription events
     */
    public WebSocketConfig(SubscriptionInterceptor subscriptionInterceptor) {
        this.subscriptionInterceptor = subscriptionInterceptor;
    }

    /**
     * Registers STOMP endpoints for WebSocket connections.
     *
     * @param stompEndpointRegistry the registry to add endpoints to
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws");
    }

    /**
     * Configures the message broker for handling messages.
     *
     * @param registry the registry to configure message broker settings
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(WebSocketTopics.Constants.TOPIC);
        // registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configures the client inbound channel with interceptors.
     *
     * @param registration the registration to add interceptors to
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(subscriptionInterceptor);
    }
}