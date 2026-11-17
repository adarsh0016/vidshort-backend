package com.aivideo.auth.controller;

import com.aivideo.auth.dto.ApiKeyRequest;
import com.aivideo.auth.dto.AuthResponse;
import com.aivideo.auth.dto.LoginRequest;
import com.aivideo.auth.dto.RegisterRequest;
import com.aivideo.auth.service.AuthService;
import com.aivideo.common.dto.ApiResponse;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and API key management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @PutMapping("/api-key")
    @Operation(summary = "Save or update Gemini API key (encrypted in DB)")
    public ResponseEntity<ApiResponse<Void>> saveApiKey(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ApiKeyRequest request) {
        authService.setApiKey(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "API key saved securely"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user info")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
