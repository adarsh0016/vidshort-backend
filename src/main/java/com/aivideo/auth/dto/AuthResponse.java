package com.aivideo.auth.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private String tokenType;
    private UUID userId;
    private String email;
    private String displayName;
    private String role;
    private boolean hasGeminiApiKey;

    public AuthResponse() {}

    public AuthResponse(String token, String tokenType, UUID userId, String email, String displayName, String role, boolean hasGeminiApiKey) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.hasGeminiApiKey = hasGeminiApiKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String tokenType = "Bearer";
        private UUID userId;
        private String email;
        private String displayName;
        private String role;
        private boolean hasGeminiApiKey;

        public Builder token(String token) { this.token = token; return this; }
        public Builder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public Builder userId(UUID userId) { this.userId = userId; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder hasGeminiApiKey(boolean hasGeminiApiKey) { this.hasGeminiApiKey = hasGeminiApiKey; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, tokenType, userId, email, displayName, role, hasGeminiApiKey);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isHasGeminiApiKey() { return hasGeminiApiKey; }
    public void setHasGeminiApiKey(boolean hasGeminiApiKey) { this.hasGeminiApiKey = hasGeminiApiKey; }
}
