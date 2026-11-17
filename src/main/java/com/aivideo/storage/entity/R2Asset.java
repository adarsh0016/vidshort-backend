package com.aivideo.storage.entity;

import com.aivideo.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "r2_assets")
public class R2Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "r2_object_key", nullable = false, unique = true, length = 500)
    private String r2ObjectKey;

    @Column(name = "asset_category", nullable = false, length = 30)
    private String assetCategory;

    @Column(name = "original_filename", length = 100)
    private String originalFilename;

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    @Column(name = "mime_type", nullable = false, length = 50)
    private String mimeType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public R2Asset() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private User user;
        private String r2ObjectKey;
        private String assetCategory;
        private String originalFilename;
        private Long fileSizeBytes;
        private String mimeType;
        private UUID referenceId;
        private String referenceType;
        private Boolean isDeleted = false;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder r2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; return this; }
        public Builder assetCategory(String assetCategory) { this.assetCategory = assetCategory; return this; }
        public Builder originalFilename(String originalFilename) { this.originalFilename = originalFilename; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder mimeType(String mimeType) { this.mimeType = mimeType; return this; }
        public Builder referenceId(UUID referenceId) { this.referenceId = referenceId; return this; }
        public Builder referenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public Builder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public R2Asset build() {
            R2Asset a = new R2Asset();
            a.setId(id);
            a.setUser(user);
            a.setR2ObjectKey(r2ObjectKey);
            a.setAssetCategory(assetCategory);
            a.setOriginalFilename(originalFilename);
            a.setFileSizeBytes(fileSizeBytes);
            a.setMimeType(mimeType);
            a.setReferenceId(referenceId);
            a.setReferenceType(referenceType);
            a.setIsDeleted(isDeleted != null ? isDeleted : false);
            return a;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getR2ObjectKey() { return r2ObjectKey; }
    public void setR2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; }
    public String getAssetCategory() { return assetCategory; }
    public void setAssetCategory(String assetCategory) { this.assetCategory = assetCategory; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public UUID getReferenceId() { return referenceId; }
    public void setReferenceId(UUID referenceId) { this.referenceId = referenceId; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean deleted) { isDeleted = deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
