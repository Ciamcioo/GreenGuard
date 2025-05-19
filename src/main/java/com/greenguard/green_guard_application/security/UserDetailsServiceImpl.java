package com.greenguard.green_guard_application.security;

import com.greenguard.green_guard_application.aspect.annotation.EnableExceptionThrowingLog;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @EnableExceptionThrowingLog
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.trace("Load user by username...");

        User user = userRepository.findById(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.trace("User loaded: {}, {}", user.getUsername(), user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
