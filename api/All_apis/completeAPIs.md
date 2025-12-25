# Splitwise API Contract (v1)

> **Authoritative API Contract**
> Backend: Spring Boot (Java)
> Frontend: Angular
> Database: MSSQL
> Base URL: `/api/v1`

---

## 1. General Conventions

### 1.1 Headers

```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

### 1.2 Standard Response Wrapper

#### Success

```json
{
  "success": true,
  "data": {},
  "error": null
}
```

#### Error

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable message"
  }
}
```

---

## 2. Authentication APIs

### 2.1 Register User

**POST** `/auth/register`

Request:

```json
{
  "name": "John Doe",
  "email": "john@mail.com",
  "phoneNo": "1234567890",
  "password": "Secret@123"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "userId": "uuid"
  }
}
```

---

### 2.2 Login

**POST** `/auth/login`

Request:

```json
{
  "email": "john@mail.com",
  "phoneNO": "1234567890",
  "password": "Secret@123"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "token": "JWT_TOKEN",
    "expiresIn": 3600
  }
}
```

---

## 3. User APIs

### 3.1 Get Current User

**GET** `/users/me`

Response:

```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "name": "John",
    "email": "john@mail.com",
    "phoneNo":"1234567890"
  }
}
```

---

## 4. Group APIs

### 4.1 Create Group

**POST** `/groups`

Request:

```json
{
  "name": "Goa Trip",
  "memberIds": ["uuid1", "uuid2"]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "groupId": "uuid"
  }
}
```

---

### 4.2 Get Group Details

**GET** `/groups/{groupId}`

Response:

```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "name": "Goa Trip",
    "members": [
      { "id": "u1", "name": "Alice" },
      { "id": "u2", "name": "Bob" }
    ]
  }
}
```

---

## 5. Expense APIs

### 5.1 Add Expense (Equal Split)

**POST** `/expenses`

Request:

```json
{
  "description": "Dinner",
  "amount": 1200,
  "paidBy": "userId",
  "groupId": "groupId",
  "splitType": "EQUAL",
  "participants": ["u1", "u2", "u3"]
}
```

---

### 5.2 Add Expense (Exact Split)

```json
{
  "description": "Hotel",
  "amount": 2000,
  "paidBy": "userId",
  "groupId": "groupId",
  "splitType": "EXACT",
  "splits": [
    { "userId": "u1", "amount": 800 },
    { "userId": "u2", "amount": 1200 }
  ]
}
```

---

### 5.3 Add Expense (Percentage Split)

```json
{
  "description": "Taxi",
  "amount": 1000,
  "paidBy": "userId",
  "groupId": "groupId",
  "splitType": "PERCENTAGE",
  "splits": [
    { "userId": "u1", "percentage": 40 },
    { "userId": "u2", "percentage": 60 }
  ]
}
```

---

## 6. Balance APIs

### 6.1 Get My Balances

**GET** `/balances`

Response:

```json
{
  "success": true,
  "data": [
    { "type": "YOU_OWE", "user": "Alice", "amount": 300 },
    { "type": "OWES_YOU", "user": "Bob", "amount": 500 }
  ]
}
```

---

### 6.2 Simplify Balances

**POST** `/balances/simplify`

Response:

```json
{
  "success": true,
  "data": null
}
```

---

## 7. Enums

### 7.1 SplitType

```
EQUAL
EXACT
PERCENTAGE
```

### 7.2 BalanceType

```
YOU_OWE
OWES_YOU
```

---

## 8. Error Codes

| Code            | Description          |
| --------------- | -------------------- |
| USER_NOT_FOUND  | User does not exist  |
| GROUP_NOT_FOUND | Group does not exist |
| INVALID_SPLIT   | Split data invalid   |
| UNAUTHORIZED    | Invalid JWT          |

---

## 9. Versioning Strategy

* URI-based versioning: `/api/v1`
* Breaking changes → `/api/v2`

---

## 10. Contract Rules (Must Follow)

* Frontend must **not assume calculations**
* Backend is **single source of truth**
* All money values are `DECIMAL(18,2)`
* UUIDs everywhere
* No API response without wrapper

---

✅ **This document is frozen and used by both frontend & backend teams
