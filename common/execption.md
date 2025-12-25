# Common Exception Handling (Global)

This document defines **industry-standard exception handling** used across all components.

---

## 1. Design Principles

* Centralized exception handling
* Consistent error response (as per API contract)
* Meaningful error codes
* No stack traces leaked to clients

---

## 2. BusinessException (Core)

```java
package com.splitwise.common.exception;

public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

---

## 3. Global Exception Handler

```java
package com.splitwise.common.exception;

import com.splitwise.dto.response.ApiError;
import com.splitwise.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGenericException(Exception ex) {
        return ApiResponse.error("INTERNAL_ERROR", "Something went wrong");
    }
}
```

---

## 4. Common Error Codes

| Code            | Meaning                  |
| --------------- | ------------------------ |
| USER_NOT_FOUND  | User does not exist      |
| EMAIL_EXISTS    | Email already registered |
| GROUP_NOT_FOUND | Group does not exist     |
| UNAUTHORIZED    | Invalid or missing token |
| INVALID_SPLIT   | Invalid expense split    |
| INTERNAL_ERROR  | Unknown server error     |

---

## 5. Flow Example

```
Service throws BusinessException
   ↓
GlobalExceptionHandler catches it
   ↓
ApiResponse.error(code, message)
   ↓
Client receives clean error JSON
```

---

## 6. Why This Works Well

* Clean separation of error logic
* Controllers stay clean
* Consistent API responses
* Easy to extend with new error codes

---

✅ Exception handling is now standardized across the project
