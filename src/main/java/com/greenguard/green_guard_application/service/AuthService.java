package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.CredentialsDTO;

public interface AuthService {

    String login(CredentialsDTO credentialsDTO);

    String signup(CredentialsDTO credentialsDTO);


}
