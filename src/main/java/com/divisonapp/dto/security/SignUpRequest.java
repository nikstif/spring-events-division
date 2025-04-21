package com.divisonapp.dto.security;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
}