package com.lee.auth.security;

import java.util.UUID;

public interface JwtProvider {
    String generateToken(UUID userId, String email, String phoneNo);
    UUID validateAndGetUserId(String token);
}
