package com.devathon.griffindor_backend.middleware;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.devathon.griffindor_backend.utils.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import jakarta.servlet.http.Cookie;

import javax.crypto.SecretKey;

public class CustomHandshakeMiddleware implements HandshakeInterceptor {

    @Override
public boolean beforeHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            String token = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token_id".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            
            if (token != null) {
                try {
                    SecretKey secretKey = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET").getBytes(StandardCharsets.UTF_8));
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                    attributes.put("token_id", token);
                } catch (Exception e) {
                    
                    return false;
                }
            } else {
                String newToken = new Jwt().generateToken();
                attributes.put("token_id", newToken);
                response.getHeaders().add("Set-Cookie", "token_id=" + newToken + "; Path=/; HttpOnly");
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(
          ServerHttpRequest request,
          ServerHttpResponse response,
          WebSocketHandler wsHandler,
          Exception exception) {
        // No-op
    }

}
