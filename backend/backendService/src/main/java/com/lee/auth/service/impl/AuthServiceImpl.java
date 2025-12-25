package com.lee.auth.service.impl;

import com.lee.auth.entity.UserEntity;
import com.lee.auth.repository.UserRepository;
import com.lee.auth.security.JwtProvider;
import com.lee.auth.service.AuthService;
import com.lee.dao.User;
import com.lee.dto.request.authenticationdtos.LoginRequest;
import com.lee.dto.request.authenticationdtos.RegisterUserRequest;
import com.lee.dto.response.authresponsedtos.AuthResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           JwtProvider jwtProvider){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void register(RegisterUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new BusinessException("EMAIL_EXISTS", "Email already registered");
        }

        if(userRepository.existsByPhoneNo(request.phoneNo())){
            throw new BusinessException("PHONE_NUMBER_EXISTS", "Phone number alreadey registered");
        }

        UserEntity user = new UserEntity(UUID.randomUUID(), request.name(), request.email(), request.phoneNo(),
                encoder.encode(request.password());
        userRepository.save(user);

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByPhoneNo(request.phoneNo()).orElseThrow(() -> new
                BusinessException("INVALID_CREDENTIALS", "Invalid phoneNo or password"));

        if(!encoder.matches(request.password(), user.getPasswordHash())){
            BusinessException("INVALID_CREDENTIALS", "Invalid phoneNo or password");
        }

        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getPhoneNo());

        return new AuthResponse(token, 3600);
    }
}
