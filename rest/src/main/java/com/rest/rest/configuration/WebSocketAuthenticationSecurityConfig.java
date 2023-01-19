package com.rest.rest.configuration;

import controller.UserHandShakeHandler;
import controller.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.naming.AuthenticationException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketAuthenticationSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    UserRepo repo;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-message")
                .setHandshakeHandler(new UserHandShakeHandler(repo))
                .withSockJS();
    }

    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "passcode";
    @Autowired
    private  WebsocketAuthenticator websocketAuthenticator;

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
                System.out.println("Auth from AuthChannelInterceptor ...");
                final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT == accessor.getCommand()) {
                    System.out.println("Command : "+accessor.getCommand());
                    final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
                    final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

                    final UsernamePasswordAuthenticationToken user;
                    try {
                        user = websocketAuthenticator.getAuthenticatedOrFail(username, password);
                        System.out.print("User : "+user);
                    } catch (AuthenticationException e) {
                        throw new RuntimeException(e);
                    }
                    accessor.setUser(user);
                }
                System.out.println("Auth from AuthChannelInterceptor Message : "+message);
                return message;
            }
        });
    }
    // TODO: For test purpose (and simplicity) i disabled CSRF, but you should re-enable this and provide a CRSF endpoint.
    @Bean
    protected boolean sameOriginDisabled() {
        return true;
    }

}
