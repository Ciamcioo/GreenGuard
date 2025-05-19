package com.greenguard.green_guard_application.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthEntryPointImplTest {

    @Test
    @DisplayName("Method commence should set the response attribute to appropriate values and has a body")
    void commenceMethodResponseSetup() throws IOException {
        AuthEntryPointImpl authEntryPoint = new AuthEntryPointImpl();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        authEntryPoint.commence(request, response, new AuthenticationServiceException("Message"));

        verify(response).setContentType("application/json");
        verify(response).setStatus(anyInt());
        verify(response.getWriter()).write(anyString());
    }
}
