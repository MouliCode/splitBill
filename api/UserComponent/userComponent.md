# User Component (Vertical Slice)

This document builds the **USER component end-to-end**, following the same industry-grade vertical slicing used for Auth.

---

## 1. User Component Scope

### API Covered

* **GET** `/api/v1/users/me`

### Responsibilities

* Resolve current authenticated user
* Fetch user details from DB
* Return safe user profile data

---

## 2. Dependency on Auth & Security

The User component **depends on Auth** for:

* JWT validation
* Current user context (userId)

Assumption:

* A `UserContext` (or `AuthenticatedUser`) is available after JWT filter execution.

---

## 3. Package Structure

```
com.splitwise.user
 ├── controller
 └── service

com.splitwise.auth
 ├── entity
 └── repository   (reused)
```

> Note: **UserEntity and UserRepository are reused** from Auth component.

---

## 4. Security Context (Shared)

### 4.1 UserContext (Common Package)

```java
package com.splitwise.common.security;

import java.util.UUID;

public class UserContext {

    private static final ThreadLocal<UUID> CURRENT_USER = new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        CURRENT_USER.set(userId);
    }

    public static UUID getUserId() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
```

---

## 5. Service Layer

### 5.1 UserService (Interface)

```java
package com.splitwise.user.service;

import com.splitwise.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUser(UUID userId);
}
```

---

### 5.2 UserServiceImpl

```java
package com.splitwise.user.service;

import com.splitwise.auth.entity.UserEntity;
import com.splitwise.auth.repository.UserRepository;
import com.splitwise.dto.response.UserResponse;
import com.splitwise.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getCurrentUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
```

---

## 6. Controller Layer

### 6.1 UserController

```java
package com.splitwise.user.controller;

import com.splitwise.common.security.UserContext;
import com.splitwise.dto.response.ApiResponse;
import com.splitwise.dto.response.UserResponse;
import com.splitwise.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        UUID currentUserId = UserContext.getUserId();
        return ApiResponse.success(userService.getCurrentUser(currentUserId));
    }
}
```

---

## 7. Request → Code → DB Flow

```
HTTP GET /users/me
   ↓
JWT Filter validates token
   ↓
UserContext.setUserId(userId)
   ↓
UserController
   ↓
UserService
   ↓
UserRepository
   ↓
MSSQL (users table)
```

---

## 8. Key Design Decisions (Industry Reasoning)

### Why reuse UserEntity & Repository?

* Single source of truth for user data
* Avoid duplicate tables / logic

### Why UserService returns DTO?

* API safety
* No password leakage

### Why controller has no parameters?

* Identity comes from JWT, not client input

---

## 9. What Is Complete Now

✅ Secure user profile API
✅ Auth → User integration
✅ Clean separation of concerns

---

## 10. Next Logical Component

➡️ **Group Component**

* Create groups
* Add members
* Uses User & Auth components
