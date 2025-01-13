package com.payway.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {
    @Schema(description = "JWT token for the user", example = "eyJhbGciOiJIUz...")
    private String token;

}

