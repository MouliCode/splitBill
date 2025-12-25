package com.lee.auth.service;

import com.lee.dto.request.authenticationdtos.LoginRequest;
import com.lee.dto.request.authenticationdtos.RegisterUserRequest;
import com.lee.dto.response.authresponsedtos.AuthResponse;

public interface AuthService {

    void register(RegisterUserRequest request);
    AuthResponse login(LoginRequest request);
}
