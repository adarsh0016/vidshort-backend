package com.aivideo.user.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_api_keys", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "provider"})
})
public class UserApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String provider = "GEMINI";

    @Column(name = "encrypted_key", nullable = false, columnDefinition = "TEXT")
    private String encryptedKey;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    public UserApiKey() {}

    public UserApiKey(UUID id, User user, String provider, String encryptedKey, Boolean isActive, Instant createdAt, Instant lastUsedAt) {
        this.id = id;
        this.user = user;
        this.provider = provider != null ? provider : "GEMINI";
        this.encryptedKey = encryptedKey;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private User user;
        private String provider = "GEMINI";
        private String encryptedKey;
        private Boolean isActive = true;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder provider(String provider) { this.provider = provider; return this; }
        public Builder encryptedKey(String encryptedKey) { this.encryptedKey = encryptedKey; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public UserApiKey build() {
            return new UserApiKey(id, user, provider, encryptedKey, isActive, null, null);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getEncryptedKey() { return encryptedKey; }
    public void setEncryptedKey(String encryptedKey) { this.encryptedKey = encryptedKey; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}
