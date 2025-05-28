package com.greenguard.green_guard_application.security;

import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailServiceImplTest {

    UserDetailsServiceImpl userDetailsService;

    UserRepository userRepository;
    User testUser;

    @BeforeEach
    void setup() {
        testUser = new User("username", "password");
        userRepository = mock(UserRepository.class);

        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Method loadUserByUsername should not return null")
    void loadUserBYUsernameShouldNotReturnNull() {
        String username = testUser.getUsername();

        when(userRepository.findById(username)).thenReturn(Optional.of(testUser));

        assertNotNull(userDetailsService.loadUserByUsername(username));
    }

    @Test
    @DisplayName("If there is not user for specified username method should throw UserNotFoundException")
    void loadUserByUsernameShouldThrowUserNotFoundException() {
        String username = "usernameNotPresentInTheDatabase";

        when(userRepository.findById(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    @DisplayName("UserDetails Object should contain the valid username and password of a user")
    void loadUserByUsernameShouldReturnCorrectUserDetailsObject() {
        String username = testUser.getUsername();

        when(userRepository.findById(username)).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(testUser.getUsername(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
    }
}