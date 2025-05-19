package com.greenguard.green_guard_application.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenGenerator implements JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenGenerator.class);

    private final String secret;
    private final long expirationTime;
    private Key key;

    public JwtTokenGenerator(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration.time}") long expirationTime)  {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        log.trace("Generating the token for user: Username: {}", username);

        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }

    public String getUsernameFromToken(String token) {
        log.trace("Retrieving username from the JWT token");

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            log.debug("Valid JWT token");

            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("JWT token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT token is empty: {}", e.getMessage());
        }
        return false;
    }

}
