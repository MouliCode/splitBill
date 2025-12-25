# Auth Component (Vertical Slice)

This document builds the **AUTH component end-to-end**, following industry-level vertical slicing.

---

## 1. Auth Component Scope

### APIs Covered

* POST `/api/v1/auth/register`
* POST `/api/v1/auth/login`

### Responsibilities

* User persistence
* Password hashing
* JWT generation
* Authentication validation

---

## 2. Package Structure

```
com.splitwise.auth
 ├── controller
 ├── service
 │    └── impl
 ├── repository
 ├── entity
 └── security
```

---

## 3. Entity Layer (Domain Model)

### 3.1 UserEntity (MSSQL-ready)

```java
package com.splitwise.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected UserEntity() {}

    public UserEntity(UUID id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    // getters only (immutability mindset)
}
```

---

## 4. Repository Layer

### 4.1 UserRepository

```java
package com.splitwise.auth.repository;

import com.splitwise.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

---

## 5. Service Layer

### 5.1 AuthService (Interface)

```java
package com.splitwise.auth.service;

import com.splitwise.dto.request.LoginRequest;
import com.splitwise.dto.request.RegisterUserRequest;
import com.splitwise.dto.response.AuthResponse;

public interface AuthService {
    void register(RegisterUserRequest request);
    AuthResponse login(LoginRequest request);
}
```

---

### 5.2 AuthServiceImpl

```java
package com.splitwise.auth.service.impl;

import com.splitwise.auth.entity.UserEntity;
import com.splitwise.auth.repository.UserRepository;
import com.splitwise.auth.service.AuthService;
import com.splitwise.dto.request.LoginRequest;
import com.splitwise.dto.request.RegisterUserRequest;
import com.splitwise.dto.response.AuthResponse;
import com.splitwise.exception.BusinessException;
import com.splitwise.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("EMAIL_EXISTS", "Email already registered");
        }

        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS", "Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException("INVALID_CREDENTIALS", "Invalid email or password");
        }

        String token = jwtProvider.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, 3600);
    }
}
```

---

## 6. Controller Layer

### 6.1 AuthController

```java
package com.splitwise.auth.controller;

import com.splitwise.auth.service.AuthService;
import com.splitwise.dto.request.LoginRequest;
import com.splitwise.dto.request.RegisterUserRequest;
import com.splitwise.dto.response.ApiResponse;
import com.splitwise.dto.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterUserRequest request) {
        authService.register(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
```

---

## 7. Security (JWT – High Level)

### 7.1 JwtProvider (Concept)

```java
public interface JwtProvider {
    String generateToken(UUID userId, String email);
    UUID validateAndGetUserId(String token);
}
```

Implementation will be added in the **Security vertical slice**.

---

## 8. Request → Code → DB Flow

### Register

```
HTTP Request
 → Controller
 → AuthService
 → UserRepository
 → MSSQL (users table)
```

### Login

```
HTTP Request
 → Controller
 → AuthService
 → PasswordEncoder
 → JwtProvider
 → HTTP Response
```

---

## 9. What Is Complete Now

✅ User entity
✅ Auth APIs
✅ Password hashing integration
✅ Clean layering

---

## 10. Next Component

➡️ **User Component** (GET /users/me)

This will reuse the UserEntity + JWT context.
