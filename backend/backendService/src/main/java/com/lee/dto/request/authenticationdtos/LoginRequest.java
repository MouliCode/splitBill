package com.lee.dto.request.authenticationdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

public record LoginRequest(
        @Email String email,
        @NotBlank String password,
        @NumberFormat String phoneNo
) {
}
