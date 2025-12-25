package com.lee.user.service;

import com.lee.auth.entity.UserEntity;
import com.lee.auth.repository.UserRepository;
import com.lee.dto.response.userresponsedtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }
    @Override
    public UserResponse getCurrentUser(UUID userId) {
        UserEntity user = repo.findById(userId).orElseThrow(() -> new BusineesException("USER_NOT_FOUND", "User not found"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhoneNo());
    }
}
