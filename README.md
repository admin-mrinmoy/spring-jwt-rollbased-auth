# 🛡️ Spring Boot JWT Role-Based Authentication

This project demonstrates a real-world implementation of **JWT (JSON Web Token) authentication** with **role-based access control** using Spring Boot and Spring Security.

---

## 📚 Overview

A secure backend API where:
- Users can **register** and **login**.
- Upon login, they receive a **JWT token**.
- This token is used to access protected resources.
- Only users with proper **roles** (like `ROLE_USER`, `ROLE_ADMIN`) can access specific endpoints.

---

## 🧱 QUICK FILE ROLES – OVERVIEW

| File                             | Responsibility                                     |
|----------------------------------|-----------------------------------------------------|
| `AuthController.java`            | Handles login/signup requests                      |
| `SignupRequest.java`, `LoginRequest.java` | Capture request data (DTOs)                    |
| `User.java`                      | Entity (with roles, username, password)            |
| `UserRepository.java`            | Talks to the DB to save/fetch user                 |
| `CustomUserDetails.java`         | Wraps your User for Spring Security                |
| `CustomUserDetailsService.java`  | Loads user by username for login                   |
| `JwtUtil.java`                   | Creates and validates JWT tokens                   |
| `JwtAuthFilter.java`             | Filter that runs before controller, validates JWT  |
| `SecurityConfig.java`            | Main Spring Security setup                         |
| `SpringJwtAuthProjectApplication.java` | App entry point                          |

---

## 📌 LAYMAN FLOW USING YOUR FILES

### ✅ 1. Signup (Register New User)

**Files Involved**:
- `AuthController.java`
- `SignupRequest.java`
- `User.java`
- `UserRepository.java`

**Flow**:
1. Client sends a `POST /signup` request:
    ```json
    {
      "username": "john",
      "password": "123456",
      "role": "ROLE_ADMIN"
    }
    ```
2. `AuthController` maps the request to `SignupRequest`.
3. A `User` object is created and password is encoded.
4. `UserRepository` saves the user to the database.

✅ **Result**: New user saved securely with a role.

---

### 🔐 2. Login (Get JWT Token)

**Files Involved**:
- `AuthController.java`
- `LoginRequest.java`
- `CustomUserDetailsService.java`
- `CustomUserDetails.java`
- `JwtUtil.java`

**Flow**:
1. Client sends a `POST /login` request:
    ```json
    {
      "username": "john",
      "password": "123456"
    }
    ```
2. `AuthenticationManager` authenticates the credentials.
3. `CustomUserDetailsService` loads the user and wraps it in `CustomUserDetails`.
4. If valid, `JwtUtil.generateToken()` creates a token.
5. The token is returned:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR..."
    }
    ```

✅ **Result**: Client receives a JWT token.

---

### 🚪 3. Accessing Protected API

**Example**:
