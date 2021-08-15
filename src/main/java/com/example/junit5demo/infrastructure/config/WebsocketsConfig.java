package com.example.junit5demo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketsConfig implements WebSocketMessageBrokerConfigurer {
    public static final String WEBSOCKETS_ENDPOINT = "/jokes-websocket";
    public static final String WEBSOCKETS_TOPIC = "/received-joke";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WEBSOCKETS_TOPIC);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKETS_ENDPOINT).withSockJS();
    }

}
