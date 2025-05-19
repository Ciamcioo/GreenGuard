package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.model.dto.CredentialsDTO;
import com.greenguard.green_guard_application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @Autowired
    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDTO credentialsDTO) {
        return ResponseEntity.ok(authService.login(credentialsDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody CredentialsDTO credentialsDTO) {
        return (ResponseEntity<?>) ResponseEntity.ok(authService.signup(credentialsDTO));
    }


}
