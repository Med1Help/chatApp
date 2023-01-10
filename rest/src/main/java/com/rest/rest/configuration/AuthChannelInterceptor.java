package com.rest.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "passcode";
    private final WebsocketAuthenticator websocketAuthenticator;

    public AuthChannelInterceptor(WebsocketAuthenticator websocketAuthenticator) {
        this.websocketAuthenticator = websocketAuthenticator;
    }

    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        System.out.println("Auth from AuthChannelInterceptor ...");
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("Command : "+accessor.getCommand());
            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

            final UsernamePasswordAuthenticationToken user;
            try {
                user = websocketAuthenticator.getAuthenticatedOrFail(username, "1234567");
                System.out.print("User : "+user);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
            accessor.setUser(user);
        }
        return message;
    }
}
