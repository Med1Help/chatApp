package com.rest.rest.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests( auth ->
                        {
                            auth.requestMatchers("/ws-message/**").permitAll();//
                            auth.requestMatchers("/api/getU").permitAll();
                            auth.requestMatchers("/api/send").permitAll();
                            auth.requestMatchers("/api/getMessage/").permitAll();
                            auth.anyRequest().authenticated();///ws-message
                        }

                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
