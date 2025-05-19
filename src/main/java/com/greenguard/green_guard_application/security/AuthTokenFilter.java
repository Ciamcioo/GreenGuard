package com.greenguard.green_guard_application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenGenerator;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthTokenFilter(JwtUtil jwtTokenGenerator, UserDetailsService userDetailsService) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.trace("Entering authTokenFilter");

        try {
            String jwt = parseJwt(request);
            logger.trace("Parsing JWT token from the header");

            if (jwt != null && jwtTokenGenerator.validateJwtToken(jwt)) {
                logger.trace("Validated JWT token.");

                String username = jwtTokenGenerator.getUsernameFromToken(jwt);
                logger.trace("Extract username from the token.");

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.trace("Loading UserDetails based on the username.");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                logger.trace("Create successfully authentication user.");

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.trace("Adding extra details about the ip and the protocol");

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.trace("Authenticated user has been added to the Security Context Holder");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: " + e.getMessage());
        }

        logger.trace("Additional filtering added");
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
