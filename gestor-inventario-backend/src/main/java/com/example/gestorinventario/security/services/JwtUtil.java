package com.example.gestorinventario.security.services;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long expiration;
    private Key secretKey;

    @PostConstruct
    public void init(){
        byte[] apiSecretBytes = new byte[64];
        new SecureRandom().nextBytes(apiSecretBytes);
        secretKey = Keys.hmacShaKeyFor(apiSecretBytes);
    }
}
