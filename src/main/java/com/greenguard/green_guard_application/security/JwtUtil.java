package com.greenguard.green_guard_application.security;

public interface JwtUtil {

    String generateToken(String username);

    String getUsernameFromToken(String token);

    boolean validateJwtToken(String token);
}
