package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.aspect.annotation.EnableExceptionThrowingLog;
import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.CredentialsDTO;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.security.JwtUtil;
import com.greenguard.green_guard_application.service.exception.UserAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authManager;
    private final PasswordEncoder passEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authManager, PasswordEncoder passEncoder,
                           UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository) {

        this.authManager = authManager;
        this.passEncoder = passEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    @EnableMethodLog
    @EnableExceptionThrowingLog
    public String login(CredentialsDTO credentialsDTO) {
        log.trace("Start of login process...");

        checkIfCredentialsAreNull(credentialsDTO);

        authManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDTO.getUsername(), credentialsDTO.getPassword()));
        log.trace("User with username: {}, is authenticated", credentialsDTO.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(credentialsDTO.getUsername());
        log.trace("UserDetails of user with username: {}, has been loaded", userDetails.getUsername());

        String token = jwtUtil.generateToken(userDetails.getUsername());
        log.debug("Jwt token generate: {}", token);

        return token;
    }

    @Override
    public String signup(CredentialsDTO credentialsDTO) {
        log.trace("Start of register user register process...");

        checkIfCredentialsAreNull(credentialsDTO);

        if (!credentialsDTO.isPasswordEncoded()) {
            String encodedOPassword = passEncoder.encode(credentialsDTO.getPassword());
            credentialsDTO.setPassword(encodedOPassword);
            credentialsDTO.setPasswordEncoded(true);
            log.trace("Password encoded");
        }

        userRepository.findById(credentialsDTO.getUsername())
                      .ifPresent(user -> {
                          log.trace("User already exists in the database");
                          throw new UserAlreadyExistsException("User already exits!");
                      });

        User user = new User(credentialsDTO.getUsername(), credentialsDTO.getPassword());
        userRepository.save(user);
        log.debug("New user created and saved");

        return user.getUsername();
    }

    private static void checkIfCredentialsAreNull(CredentialsDTO credentialsDTO) {
        if (Objects.isNull(credentialsDTO)) {
            log.trace("Argument is null");
            throw new IllegalArgumentException();
        }
        log.trace("Not null credentials");
    }
}
