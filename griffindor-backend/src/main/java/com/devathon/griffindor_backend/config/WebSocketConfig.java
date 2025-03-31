package com.devathon.griffindor_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import com.devathon.griffindor_backend.middleware.CustomHandshakeMiddleware;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketRoutes.ENDPOINT_APP).addInterceptors(new CustomHandshakeMiddleware())
                .setAllowedOrigins("*");
        // .setHeartbeatTime(25000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(WebSocketRoutes.GET_PREFIX);
        registry.enableSimpleBroker(WebSocketRoutes.TOPIC_PREFIX, WebSocketRoutes.QUEUE_PREFIX);
        registry.setUserDestinationPrefix(WebSocketRoutes.USER_PREFIX);
    }
}
