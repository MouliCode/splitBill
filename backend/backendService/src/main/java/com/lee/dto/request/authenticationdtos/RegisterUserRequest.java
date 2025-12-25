package com.lee.dto.request.authenticationdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

public record RegisterUserRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NumberFormat @NotBlank String phoneNo,
        @NotBlank String password) {
}
