package com.rest.rest.configuration;
import controller.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Configuration
@Component
public class MyAuthProvider implements AuthenticationProvider {
    @Autowired
    private UserRepo repo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.println("\nIn MyAuthProvider.authenticate(): ");

        // Get the User from UserDetailsService
        String providedUsername = authentication.getPrincipal().toString();
        var user = repo.findByUserName(providedUsername).get(0);

        System.out.println("User Details from UserService based on username-" + providedUsername + " : " + user);

        String providedPassword = authentication.getCredentials().toString();
        String correctPassword = user.getPass();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Provided Password - " + providedPassword + " Correct Password: " + correctPassword);

        // Authenticate
        // If Passwords don't match throw and exception
        if(!encoder.matches(providedPassword, user.getPass())) throw new RuntimeException("Incorrect Credentials");

        System.out.println("Passwords Match....\n");

        // return Authentication Object
        Authentication authenticationResult =
                new UsernamePasswordAuthenticationToken(user, authentication.getCredentials());
        return authenticationResult;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println("\nIn MyAuthProvider.supports(): ");
        System.out.println("Checking whether MyAuthProvider supports Authentication type\n");
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
