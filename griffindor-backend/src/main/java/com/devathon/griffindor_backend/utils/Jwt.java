package com.devathon.griffindor_backend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class Jwt {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String sessionId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 1000 * 60 * 60;

        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(secretKey)
                .compact();
    }

    public String validateAndGetUser(String token) {
        System.out.println("🔁 token: " + token);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractTokenId(StompHeaderAccessor headerAccessor) {
        Object connectMessageObj = headerAccessor.getHeader("simpConnectMessage");

        if (connectMessageObj instanceof GenericMessage<?> connectMessage) {
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionAttributes = (Map<String, Object>) connectMessage
                    .getHeaders()
                    .get("simpSessionAttributes");

            if (sessionAttributes != null) {
                return (String) sessionAttributes.get("token_id");
            }
        }

        return null;
    }
}
