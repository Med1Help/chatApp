package com.rest.rest.configuration;

import controller.Login;
import controller.User;
import controller.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.List;

@Component
public class WebsocketAuthenticator {
    @Autowired
    UserRepo repo ;
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String  username, final String password) throws AuthenticationException {
        System.out.println("Authentication ... "+username+" pass : "+password);
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        List<User> users = (List<User>) repo.findByUserNameAndPass(username,password);
        System.out.println(users);
        if(users.isEmpty()){
            throw new BadCredentialsException("Bad credentials for user " + username);
        }
        User user = users.get(0);
        System.out.println("No user found from webSocket");

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
