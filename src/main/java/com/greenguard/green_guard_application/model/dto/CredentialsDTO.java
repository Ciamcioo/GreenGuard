package com.greenguard.green_guard_application.model.dto;

import java.util.Objects;

public final class CredentialsDTO {
    private final String username;
    private String password;
    boolean isPasswordEncoded;

    public CredentialsDTO(String username, String password, boolean isPasswordEncoded) {
        this.username = username;
        this.password = password;
        this.isPasswordEncoded = isPasswordEncoded;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordEncoded() {
        return isPasswordEncoded;
    }

    public void setPasswordEncoded(boolean passwordEncoded) {
        isPasswordEncoded = passwordEncoded;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CredentialsDTO that = (CredentialsDTO) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
