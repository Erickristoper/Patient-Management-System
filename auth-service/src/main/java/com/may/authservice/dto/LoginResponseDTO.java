package com.may.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginResponseDTO {

    private final String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
