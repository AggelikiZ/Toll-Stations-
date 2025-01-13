package com.payway.models;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.scheduling.support.SimpleTriggerContext;

public class LoginRequest {
    @Schema(description = "User's username", example = "username1")
    private final String username;

    @Schema(description = "User's password", example = "password1")
    private final String password;

    public LoginRequest(String username, String password) {
        this.username =username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    // Getters and Setters
}
