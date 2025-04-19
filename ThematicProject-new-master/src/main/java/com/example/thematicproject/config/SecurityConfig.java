package com.example.thematicproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Slå CSRF fra, så du kan sende POST fra fx Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Tillad alle forespørgsler uden login
                );
        return http.build();
    }


}
