package com.lee.user.controller;

import com.lee.common.security.UserContext;
import com.lee.dto.response.apiResponsedtos.ApiResponse;
import com.lee.dto.response.userresponsedtos.UserResponse;
import com.lee.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {this.service = service;}

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(){
        UUID currentUserId = UserContext.getUserId();
        return ApiResponse.success(service.getCurrentUser(currentUserId));
    }
}
