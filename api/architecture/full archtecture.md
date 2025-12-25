
# Splitwise Clone – Full Project Documentation

## 1. Overview

This document defines a **Splitwise-like expense sharing application** using:

* **Backend**: Java, Spring Boot
* **Frontend**: Angular
* **Database**: **Microsoft SQL Server (MSSQL)**
* **Architecture**: Layered Architecture + REST
* **Design Patterns**: Applied throughout (listed below)

The goal is to build a **production-grade, scalable, and cleanly designed system**.

---

## 2. High-Level Architecture

### 2.1 System Components

```
Angular Frontend
      |
 REST API (JSON)
      |
Spring Boot Backend
  ├── Controller Layer
  ├── Service Layer
  ├── Domain Layer
  ├── Repository Layer
  └── Infrastructure Layer
      |
 Relational Database
```

### 2.2 Design Patterns Used

| Pattern             | Usage                                    |
| ------------------- | ---------------------------------------- |
| MVC                 | Controller → Service → Repository        |
| DTO                 | API request/response isolation           |
| Factory             | Expense split creation                   |
| Strategy            | Split types (Equal, Exact, Percentage)   |
| Singleton           | Configuration beans                      |
| Builder             | Complex DTO creation                     |
| Observer (Optional) | Notifications                            |
| Repository          | DB abstraction                           |
| Adapter             | External services (email, notifications) |

---

## 3. Core Features

* User Registration & Login
* Create Groups
* Add Expenses
* Multiple Split Types
* Track Balances
* Simplify Debts
* Expense History
* User-level & Group-level balances

---

## 4. Domain Model

### 4.1 Entities

#### User

```
User
- id: UUID
- name: String
- email: String
- passwordHash: String
- createdAt
```

#### Group

```
Group
- id: UUID
- name: String
- createdBy: User
- members: List<User>
```

#### Expense

```
Expense
- id: UUID
- description
- amount
- paidBy: User
- group: Group (nullable)
- splitType: SplitType
- splits: List<ExpenseSplit>
- createdAt
```

#### ExpenseSplit

```
ExpenseSplit
- id
- user
- amountOwed
```

#### Balance

```
Balance
- fromUser
- toUser
- amount
```

---

## 5. Split Strategy Pattern

### 5.1 Strategy Interface

```
SplitStrategy
+ validate()
+ calculateSplits()
```

### 5.2 Implementations

* EqualSplitStrategy
* ExactSplitStrategy
* PercentageSplitStrategy

### 5.3 Factory Pattern

```
SplitStrategyFactory
+ getStrategy(SplitType)
```

---

## 6. Backend – Spring Boot Design

### 6.1 Package Structure

```
com.splitwise
 ├── controller
 ├── service
 ├── repository
 ├── domain
 ├── dto
 ├── factory
 ├── strategy
 ├── exception
 └── config
```

---

## 7. REST API Documentation

### 7.1 Authentication APIs

#### Register User

```
POST /api/auth/register
```

Request:

```
{
  "name": "John",
  "email": "john@mail.com",
  "password": "secret"
}
```

Response:

```
201 CREATED
```

---

#### Login

```
POST /api/auth/login
```

Response:

```
{
  "token": "JWT_TOKEN"
}
```

---

### 7.2 User APIs

#### Get Current User

```
GET /api/users/me
```

---

### 7.3 Group APIs

#### Create Group

```
POST /api/groups
```

Request:

```
{
  "name": "Trip to Goa",
  "memberIds": ["uuid1", "uuid2"]
}
```

---

#### Get Group Details

```
GET /api/groups/{groupId}
```

---

### 7.4 Expense APIs

#### Add Expense

```
POST /api/expenses
```

Request:

```
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

#### Add Expense (Percentage Split)

```
{
  "splitType": "PERCENTAGE",
  "splits": [
    { "userId": "u1", "percentage": 40 },
    { "userId": "u2", "percentage": 60 }
  ]
}
```

---

### 7.5 Balance APIs

#### Get User Balances

```
GET /api/balances
```

Response:

```
[
  { "youOwe": "Alice", "amount": 300 },
  { "owesYou": "Bob", "amount": 500 }
]
```

---

#### Simplify Debts

```
POST /api/balances/simplify
```

---

## 8. Service Layer Responsibilities

| Service        | Responsibility                |
| -------------- | ----------------------------- |
| UserService    | User management               |
| GroupService   | Group operations              |
| ExpenseService | Expense creation & validation |
| BalanceService | Balance calculation           |
| AuthService    | JWT authentication            |

---

## 9. Exception Handling

```
@RestControllerAdvice
- UserNotFoundException
- InvalidSplitException
- UnauthorizedException
```

Standard error response:

```
{
  "errorCode": "INVALID_SPLIT",
  "message": "Percentages do not sum to 100"
}
```

---

## 10. Security Design

* JWT Authentication
* Password hashing (BCrypt)
* Role-based authorization
* Spring Security Filters

---

## 11. Frontend – Angular Architecture

### 11.1 Module Structure

```
app
 ├── core
 ├── auth
 ├── dashboard
 ├── groups
 ├── expenses
 ├── services
 └── shared
```

### 11.2 Angular Services

* AuthService
* UserService
* GroupService
* ExpenseService
* BalanceService

---

## 12. Frontend Flow

1. User logs in
2. Dashboard loads balances
3. Create group
4. Add expense
5. Backend recalculates balances
6. UI updates

---

## 13. Database Schema (MSSQL – Simplified)

### 13.1 MSSQL Notes

* Use `UNIQUEIDENTIFIER` for UUIDs
* Use `DECIMAL(18,2)` for money
* Use `DATETIME2` for timestamps
* Enable proper indexing for FK columns

### 13.2 Tables

```sql
CREATE TABLE users (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    created_at DATETIME2 DEFAULT SYSDATETIME()
);

CREATE TABLE groups (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    created_by UNIQUEIDENTIFIER,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE group_members (
    group_id UNIQUEIDENTIFIER,
    user_id UNIQUEIDENTIFIER,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE expenses (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    description NVARCHAR(255),
    amount DECIMAL(18,2) NOT NULL,
    paid_by UNIQUEIDENTIFIER,
    group_id UNIQUEIDENTIFIER,
    created_at DATETIME2 DEFAULT SYSDATETIME(),
    FOREIGN KEY (paid_by) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE expense_splits (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    expense_id UNIQUEIDENTIFIER,
    user_id UNIQUEIDENTIFIER,
    amount_owed DECIMAL(18,2),
    FOREIGN KEY (expense_id) REFERENCES expenses(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE balances (
    from_user UNIQUEIDENTIFIER,
    to_user UNIQUEIDENTIFIER,
    amount DECIMAL(18,2),
    PRIMARY KEY (from_user, to_user)
);
```

---

## 14. Future Enhancements

* Expense comments
* Notifications
* Currency support
* File uploads
* Offline sync

---

## 15. Development Best Practices

* SOLID principles
* DTO validation
* Unit tests (JUnit + Mockito)
* Integration tests
* OpenAPI (Swagger)
* CI/CD pipeline

---

## 16. Swagger Configuration

```
/swagger-ui.html
```

---

## 17. Conclusion

This design follows **clean architecture, strong design patterns, and scalable principles**, making it suitable for real-world applications.

---

If you want next:

* Full backend code
* Angular UI screens
* Database scripts
* Interview-ready explanation
* Low-level design (LLD)
