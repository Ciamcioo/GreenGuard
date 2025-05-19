package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.CredentialsDTO;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.security.JwtUtil;
import com.greenguard.green_guard_application.service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    AuthService authService;

    AuthenticationManager authManager;
    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;
    JwtUtil jwtTokenGenerator;
    UserRepository userRepository;

    CredentialsDTO validCredentialsDTO;
    CredentialsDTO encdoedCredentialsDTO;

    // Decoded password: admin
    String encodedPassword = "$2a$10$YLMG97UK53w.fAkh2Ys/uel3yvNtTg0SfpAbpoan0nBIgVmHtv8y6";

    @BeforeEach
    void setup() {
        validCredentialsDTO = new CredentialsDTO("username", "password", false);
        encdoedCredentialsDTO = new CredentialsDTO("admin", encodedPassword, true);

        authManager = mock(AuthenticationManager.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenGenerator = mock(JwtUtil.class);
        userRepository = mock(UserRepository.class);


        when(jwtTokenGenerator.generateToken(anyString())).thenReturn("token");

        authService = new AuthServiceImpl(authManager, passwordEncoder, userDetailsService, jwtTokenGenerator, userRepository);
    }



    @Test
    @DisplayName("Login method should not return null value")
    void returnNotNullValueForLoginMethod() {
        assertNotNull(authService.login(validCredentialsDTO));
        assertThrows(IllegalArgumentException.class, () -> authService.login(null));
    }

    @Test
    @DisplayName("If a password is not encoded, the login method should perform encoding")
    void passwordEncodingShouldBePerformedIfThePasswordIsNotEncoded() {
        assertFalse(validCredentialsDTO.isPasswordEncoded());
        authService.login(validCredentialsDTO);
        assertTrue(validCredentialsDTO.isPasswordEncoded());
    }

    @Test
    @DisplayName("If a password is encoded, the login method should not perform encoding of credentials")
    void passwordEncodingShouldNotBeInvokedIfThePasswordIsEncoded() {
        authService.login(encdoedCredentialsDTO);
        verify(passwordEncoder, never()).encode(encdoedCredentialsDTO.getPassword());

    }

    @Test
    @DisplayName("If user is authenticated, the login method should return String")
    void authenticatedCredentialsForLoginMethodShouldReturnString() {
        assertInstanceOf(String.class, authService.login(validCredentialsDTO));
        assertInstanceOf(String.class, authService.login(encdoedCredentialsDTO));


        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken(validCredentialsDTO.getUsername(), validCredentialsDTO.getPassword()));
        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken(encdoedCredentialsDTO.getUsername(), encdoedCredentialsDTO.getPassword()));
    }

    @Test
    @DisplayName("If UserDetails has been loaded correctly, the login method should return String")
    void userDetailsHasBennLoadedReturnString() {
        assertInstanceOf(String.class, authService.login(validCredentialsDTO));
        assertInstanceOf(String.class, authService.login(encdoedCredentialsDTO));

        verify(userDetailsService).loadUserByUsername(validCredentialsDTO.getUsername());
        verify(userDetailsService).loadUserByUsername(encdoedCredentialsDTO.getUsername());
    }

    @Test
    @DisplayName("Login should return generated token")
    void generateTokenForValidUser() {
        String validToken = "validJWToken123";

        when(jwtTokenGenerator.generateToken(validCredentialsDTO.getUsername())).thenReturn(validToken);
        when(jwtTokenGenerator.generateToken(encdoedCredentialsDTO.getUsername())).thenReturn(validToken);

        assertEquals(validToken, authService.login(validCredentialsDTO));
        assertEquals(validToken, authService.login(encdoedCredentialsDTO));
    }

    @Test
    @DisplayName("Signup method should not return null value")
    void signupMethodShouldNotReturnNullValue() {
        assertNotNull(authService.signup(validCredentialsDTO));
        assertThrows(IllegalArgumentException.class, () -> authService.signup(null));
    }

    @Test
    @DisplayName("If a password is encoded, the signup method should not perform encoding")
    void signupMethodDoNotPerformEncryption() {
        authService.signup(encdoedCredentialsDTO);
        verify(passwordEncoder, never()).encode(encdoedCredentialsDTO.getPassword());
    }

    @Test
    @DisplayName("If a password is not encoded, the signup method should perform encoding")
    void signupMethodShouldPerformEncoding() {
        assertFalse(validCredentialsDTO.isPasswordEncoded());
        assertInstanceOf(String.class, authService.signup(validCredentialsDTO));
        assertTrue(validCredentialsDTO.isPasswordEncoded());
    }

    @Test
    @DisplayName("User with specified credentials exists in the database method should throw exception UserAlreadyExists")
    void userWithCredentialsWhichExistsInTheDatabaseShouldThrowUserAlreadyExistsException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User(validCredentialsDTO.getUsername(), validCredentialsDTO.getPassword())));

        assertThrows(UserAlreadyExistsException.class, () -> authService.signup(validCredentialsDTO));

        verify(userRepository).findById(anyString());
    }

    @Test
    @DisplayName("Method signup() should not throw exception, if credentials are not in the database")
    void credentialsNotPresentInDatabaseAsArgumentShouldNotCauseException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertInstanceOf(String.class, authService.signup(validCredentialsDTO));

        verify(userRepository).findById(anyString());
    }

    @Test
    @DisplayName("Method signup() should perform save of a new user to the database and return the username of saved user")
    void newUserShouldBeSavedToTheDatabase() {
        User user = new User(validCredentialsDTO.getUsername(), validCredentialsDTO.getPassword());

        assertEquals(validCredentialsDTO.getUsername(), authService.signup(validCredentialsDTO));
        verify(userRepository).save(user);
    }



}
