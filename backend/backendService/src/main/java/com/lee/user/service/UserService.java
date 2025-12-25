package com.lee.user.service;

import com.lee.dto.response.userresponsedtos.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse getCurrentUser(UUID userId);
}
