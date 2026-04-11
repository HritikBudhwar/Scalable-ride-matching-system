package com.platform.controller;

import com.platform.dto.request.LoginRequestDTO;
import com.platform.dto.response.LoginResponseDTO;
import com.platform.model.user.Administrator;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.model.user.User;
import com.platform.repository.UserRepository;
import com.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Authentication controller for login/logout operations
 * Demo mode: plain-text password check (no BCrypt/JWT)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST /api/auth/login
     * Login with email, password, and user type
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            String email = request.getEmail();
            String requestedType = request.getUserType();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

            if (user.getPasswordHash() == null || !user.getPasswordHash().equals(request.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials.");
            }

            String actualType = resolveUserType(user);
            if (requestedType != null && !requestedType.equalsIgnoreCase(actualType)) {
                throw new IllegalArgumentException("User type mismatch. Try logging in as " + actualType + ".");
            }

            String token = authService.generateToken(user.getId(), actualType);
            
            // Create response
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setUserId(user.getId());
            response.setUserType(actualType);
            response.setEmail(email);
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setMessage("Login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(null, null, null, null, null, null, 
                    "Login failed: " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/logout
     * Logout the current user
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                authService.invalidateToken(token);
            }
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "Logged out"));
        }
    }

    /**
     * GET /api/auth/validate
     * Validate if token is still valid
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "No token provided"));
            }

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (authService.validateToken(token)) {
                Map<String, Object> userInfo = authService.getUserInfo(token);
                return ResponseEntity.ok(userInfo);
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Token validation failed"));
        }
    }

    private String resolveUserType(User user) {
        if (user instanceof Customer) return "CUSTOMER";
        if (user instanceof Driver) return "DRIVER";
        if (user instanceof Administrator) return "ADMIN";
        return "USER";
    }
}
