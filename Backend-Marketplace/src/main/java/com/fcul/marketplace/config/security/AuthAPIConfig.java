package com.fcul.marketplace.config.security;

import com.auth0.client.auth.AuthAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;


@Configuration
public class AuthAPIConfig {

    @Value("${auth0.client-id}")
    public String clientId;
    @Value("${auth0.domain}")
    public String domain;
    @Value("${auth0.client-secret}")
    public String secret;

    @Autowired
    private GenericApplicationContext context;

    @Bean
    public AuthAPI authAPI() {
        return AuthAPI.newBuilder(domain, clientId, secret).build();
    }
}