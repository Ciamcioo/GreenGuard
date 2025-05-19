package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.security.AuthTokenFilter;
import com.greenguard.green_guard_application.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {

    AuthTokenFilter authFilter;

    JwtUtil jwtTokenGenerator;
    UserDetailsService userDetailsService;

    MockHttpServletRequest request;
    HttpServletResponse response;
    FilterChain filterChain;

    String username = "username";
    String authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY4MTY1ODQyMywiZXhwIjoxNjgxNjYyMDIzfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY4MTY1ODQyMywiZXhwIjoxNjgxNjYyMDIzfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";


    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJleGFtcGxlVXNlciIsImlhdCI6MTY4MTY1ODQyMywiZXhwIjoxNjgxNjYyMDIzfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        jwtTokenGenerator = mock(JwtUtil.class);
        userDetailsService = mock(UserDetailsService.class);

        when(jwtTokenGenerator.validateJwtToken(token)).thenReturn(true);
        when(jwtTokenGenerator.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(new org.springframework.security.core.userdetails.User(username, "password", Collections.emptyList()));

        authFilter = new AuthTokenFilter(jwtTokenGenerator, userDetailsService);
    }

    @Test
    @DisplayName("Method doFilterInternal for AuthTokenFilter should validate token form request")
    void validateTokenFromTheRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method doFilterInternal = AuthTokenFilter.class.getDeclaredMethod("doFilterInternal",
                                                                            HttpServletRequest.class,
                                                                            HttpServletResponse.class,
                                                                            FilterChain.class);
        doFilterInternal.setAccessible(true);

        doFilterInternal.invoke(authFilter, request, response, filterChain);

        verify(jwtTokenGenerator).validateJwtToken(token);

    }

    @Test
    @DisplayName("Method doFilterInternal of AuthTokenFilter should loadUserByUsername")
    void loadUserByUsernameInDoFilterInternal() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method doFilterInternal = AuthTokenFilter.class.getDeclaredMethod("doFilterInternal",
                HttpServletRequest.class,
                HttpServletResponse.class,
                FilterChain.class);
        doFilterInternal.setAccessible(true);


        doFilterInternal.invoke(authFilter, request, response, filterChain);

        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    @DisplayName("Method doFilterInternal of AuthTokenFilter should set authentication fo SecurityContextHolder")
    void setAuthenticationForSecurityContextHolderInDoInternalFilter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method doFilterInternal = AuthTokenFilter.class.getDeclaredMethod("doFilterInternal",
                HttpServletRequest.class,
                HttpServletResponse.class,
                FilterChain.class);
        doFilterInternal.setAccessible(true);

        doFilterInternal.invoke(authFilter, request, response, filterChain);

        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    @DisplayName("Method doFilterInternal of AuthTokenFilter should invoke next filter in the filter chain")
    void doFilterInternalShouldInvokeNextFilterInTheFilterChain() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ServletException, IOException {
        Method doFilterInternal = AuthTokenFilter.class.getDeclaredMethod("doFilterInternal",
                HttpServletRequest.class,
                HttpServletResponse.class,
                FilterChain.class);
        doFilterInternal.setAccessible(true);

        doFilterInternal.invoke(authFilter, request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("If in the request there is no authorization header parseJwt() should return null")
    void lackOfTheAuthorizationHeaderInTheRequestReturnNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method parseJwt = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwt.setAccessible(true);

        request.removeHeader("Authorization");

        assertNull(parseJwt.invoke(authFilter, request));
    }


    @Test
    @DisplayName("If in the request, the header of Authorization do not starts with the \"Bearer \" method should return null")
    void authorizationHeaderShouldStartWithSpecificString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method parseJwt = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwt.setAccessible(true);

        request.removeHeader("Authorization");
        request.addHeader("Authorization", "String for the test");

        assertNull(parseJwt.invoke(authFilter, request));
    }

    @Test
    @DisplayName("If the request header is correct the method praseJwt() should return instance of String")
    void parseJwtShouldReturnInstanceOfString() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method parseJwt = AuthTokenFilter.class.getDeclaredMethod("parseJwt", HttpServletRequest.class);
        parseJwt.setAccessible(true);

        assertInstanceOf(String.class, parseJwt.invoke(authFilter, request));
    }
}
