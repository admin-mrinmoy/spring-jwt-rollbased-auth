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
GET /api/admin/dashboard
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR...

**Files Involved**:
- `JwtAuthFilter.java`
- `JwtUtil.java`
- `SecurityConfig.java`

**Flow**:
1. `JwtAuthFilter` intercepts the request.
2. Extracts and validates the token from header.
3. If valid, `JwtUtil` extracts username and roles.
4. `CustomUserDetailsService` loads the user again.
5. Spring sets authentication context with roles.

✅ **Result**: User is authenticated and roles are attached.

---

### ✅ 4. Spring Security Role Access Check

**File Involved**: `SecurityConfig.java`

```java
.authorizeHttpRequests()
  .requestMatchers("/api/admin/**").hasRole("ADMIN")
  .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
  .anyRequest().authenticated()
Flow:

Spring checks if the role from token (e.g., ROLE_ADMIN) matches the route.

If yes → allow access.

If not → return 403 Forbidden.

##Flie Wise Summary##
| File                                      | Summary                                                                                   |
| ----------------------------------------- | ----------------------------------------------------------------------------------------- |
| `AuthController.java`                     | Main controller for `/signup` and `/login`. Talks to repo, auth manager, and JWT utility. |
| `SignupRequest.java`, `LoginRequest.java` | Simple DTOs for mapping JSON to Java objects.                                             |
| `User.java`                               | Entity mapped to DB, contains username, password, and roles.                              |
| `UserRepository.java`                     | Interface to DB. Finds user by username.                                                  |
| `CustomUserDetails.java`                  | Wraps `User` and implements Spring Security's `UserDetails`.                              |
| `CustomUserDetailsService.java`           | Loads user from DB during login.                                                          |
| `JwtUtil.java`                            | Creates/validates JWT. Extracts claims.                                                   |
| `JwtAuthFilter.java`                      | Filter that validates the token and sets authenticated user in context.                   |
| `SecurityConfig.java`                     | Main Spring Security config: filters, access rules, and auth provider.                    |
| `SpringJwtAuthProjectApplication.java`    | Application entry point.                                                                  |

#🎯 VISUAL FLOW SUMMARY#
```[Client] ----> /signup (SignupRequest)
                |
           [AuthController] --> save to DB (User, UserRepository)

[Client] ----> /login (LoginRequest)
                |
           [AuthController] --> authenticate
                |                     |
       [AuthenticationManager]    [CustomUserDetailsService]
                |                     |
            if valid              return UserDetails
                |
             [JwtUtil] --> create token
                |
           return token to client

[Client] ---> /api/admin/xxx (with token)
               |
         [JwtAuthFilter] intercepts
               |
           [JwtUtil] validates token
               |
     [CustomUserDetailsService] loads user again
               |
    [SecurityContext] updated with user + roles

     → Controller runs only if role matches in [SecurityConfig]```
##🧠 Final Notes
All passwords are securely encoded using BCrypt.

Tokens are stateless: no DB call required to validate them (unless for user re-fetch).

Easy to extend with more roles (e.g., MODERATOR, SUPERADMIN).

Clean separation of concerns between controller, security, and utility.
