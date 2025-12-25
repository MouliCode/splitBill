package com.lee.dto.response.authresponsedtos;

public record AuthResponse(String token, long expiresIn) {
}
