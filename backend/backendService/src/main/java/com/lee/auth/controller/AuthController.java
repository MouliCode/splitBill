package com.lee.auth.controller;

import com.lee.auth.service.AuthService;
import com.lee.dto.request.authenticationdtos.LoginRequest;
import com.lee.dto.request.authenticationdtos.RegisterUserRequest;
import com.lee.dto.response.apiResponsedtos.ApiResponse;
import com.lee.dto.response.authresponsedtos.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService service){
        this.authService = service;
    }

    @PostMapping("/register")
    public ApiResponse<void> register(@Valid @RequestBody RegisterUserRequest request){
        authService.register(request);
        return ApiResponse.success(request);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ApiResponse.success(authService.login(request));
    }
}
