
# DTO Definitions (API Layer)s

    dto/
    ├── request/
    │    ├── RegisterUserRequest
    │    ├── LoginRequest
    │    ├── CreateGroupRequest
    │    ├── AddExpenseRequest
    │    └── SplitRequest
    └── response/
        ├── ApiResponse<T>
        ├── AuthResponse
        ├── GroupResponse
        ├── BalanceResponse



> This document defines **all Request & Response DTOs** derived strictly from the API contract.
> These DTOs are used **only at the API boundary (Controller layer)**.

---

## 1. Design Rules (Industry Standard)

* DTOs are **immutable**
* No JPA annotations
* No business logic
* Validation annotations allowed
* Java `record` used where possible (Java 17+)

---

## 2. Common DTOs

### 2.1 ApiResponse<T>

```java
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message));
    }
}
```

### 2.2 ApiError

```java
public record ApiError(
        String code,
        String message
) {}
```

---

## 3. Authentication DTOs

### 3.1 RegisterUserRequest

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password
) {}
```

### 3.2 LoginRequest

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {}
```

### 3.3 AuthResponse

```java
public record AuthResponse(
        String token,
        long expiresIn
) {}
```

---

## 4. User DTOs

### 4.1 UserResponse

```java
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email
) {}
```

---

## 5. Group DTOs

### 5.1 CreateGroupRequest

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record CreateGroupRequest(
        @NotBlank String name,
        @NotEmpty List<UUID> memberIds
) {}
```

### 5.2 GroupMemberResponse

```java
import java.util.UUID;

public record GroupMemberResponse(
        UUID id,
        String name
) {}
```

### 5.3 GroupResponse

```java
import java.util.List;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String name,
        List<GroupMemberResponse> members
) {}
```

---

## 6. Expense DTOs

### 6.1 AddExpenseRequest

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record AddExpenseRequest(
        @NotBlank String description,
        @NotNull BigDecimal amount,
        @NotNull UUID paidBy,
        UUID groupId,
        @NotNull SplitType splitType,
        List<UUID> participants,
        List<SplitRequest> splits
) {}
```

### 6.2 SplitRequest

```java
import java.math.BigDecimal;
import java.util.UUID;

public record SplitRequest(
        UUID userId,
        BigDecimal amount,
        Integer percentage
) {}
```

---

## 7. Balance DTOs

### 7.1 BalanceResponse

```java
import java.math.BigDecimal;

public record BalanceResponse(
        BalanceType type,
        String user,
        BigDecimal amount
) {}
```

---

## 8. Enums

### 8.1 SplitType

```java
public enum SplitType {
    EQUAL,
    EXACT,
    PERCENTAGE
}
```

### 8.2 BalanceType

```java
public enum BalanceType {
    YOU_OWE,
    OWES_YOU
}
```

---

## 9. DTO → Layer Mapping

| DTO                 | Used In           |
| ------------------- | ----------------- |
| RegisterUserRequest | AuthController    |
| LoginRequest        | AuthController    |
| AddExpenseRequest   | ExpenseController |
| SplitRequest        | ExpenseService    |
| GroupResponse       | GroupController   |
| BalanceResponse     | BalanceController |

---

## 10. Important Notes

* Controllers accept **only request DTOs**
* Controllers return **only ApiResponse<T>**
* Services never return entities to controllers
* Mapping handled via mapper classes (next step)

---

✅ DTO layer is now **locked & stable**

