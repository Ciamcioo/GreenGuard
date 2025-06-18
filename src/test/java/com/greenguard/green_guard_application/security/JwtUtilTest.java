package com.greenguard.green_guard_application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath*:application-test.properties")
public class JwtUtilTest {

    // helper fields
    @Value(value = "${jwt.secret}") private String secret;
    @Value(value = "${jwt.expiration.time}")  private long expirationTime;
    private final String username = "username1";
    private String token;
    private Key key;

    // tested field
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        token = jwtUtil.generateToken(username);
    }

    @Test
    @DisplayName("Method generateToken() should not return null value")
    void generateTokenReturnsNotNull() {
        assertNotNull(token);
    }

    @Test
    @DisplayName("Method generateToken() as subject should use a username")
    void generateTokenShouldAsSubjectSetUsername() {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        assertEquals(username, claims.getSubject());
    }

    @Test
    @DisplayName("Method generateToken() should generate token for one hour")
    void generateTokenShouldProvideTokenForOneHour() {
        token = jwtUtil.generateToken(username);

        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        assertTrue(new Date().before(claims.getExpiration()));
    }

    @Test
    @DisplayName("Method getUsernameFromToken should return correct username for specific token")
    void getUsernameFromTokenShouldReturnUserAssociatedWithTheToken() {
        token = jwtUtil.generateToken(username);

        String resultUsername = jwtUtil.getUsernameFromToken(token);

        assertEquals(username, resultUsername);
    }

    @Test
    @DisplayName("Method getUsernameFromToken should return different username for token of other user")
    void getUsernameShouldReturnUniqueTokenForUsers() {
        token = jwtUtil.generateToken(username);

        assertNotEquals("username112", jwtUtil.getUsernameFromToken(token));
    }


    @Test
    @DisplayName("Method validateToken should return false, the token is not valid")
    void tryToValidateExpiredToken() throws InterruptedException {
        token = jwtUtil.generateToken(username);
        Thread.sleep(1500L);

        assertFalse(jwtUtil.validateJwtToken(token));
    }

    @Test
    @DisplayName("Method validateToken should return true, the token is valid")
    void performAValidationOfAValidToken() throws InterruptedException {
        token = jwtUtil.generateToken(username);
        Thread.sleep(new Random().nextLong(expirationTime - (3*expirationTime/10)) + expirationTime/20);

        assertTrue(jwtUtil.validateJwtToken(token));
    }

}
