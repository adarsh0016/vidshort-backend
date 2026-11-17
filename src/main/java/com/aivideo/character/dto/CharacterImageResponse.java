package com.aivideo.character.dto;

import java.time.Instant;
import java.util.UUID;

public class CharacterImageResponse {
    private UUID id;
    private String r2Url;
    private String r2ObjectKey;
    private String imageType;
    private boolean isPrimary;
    private String originalFilename;
    private Long fileSizeBytes;
    private Instant createdAt;

    public CharacterImageResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String r2Url;
        private String r2ObjectKey;
        private String imageType;
        private boolean isPrimary;
        private String originalFilename;
        private Long fileSizeBytes;
        private Instant createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder r2Url(String r2Url) { this.r2Url = r2Url; return this; }
        public Builder r2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; return this; }
        public Builder imageType(String imageType) { this.imageType = imageType; return this; }
        public Builder isPrimary(boolean isPrimary) { this.isPrimary = isPrimary; return this; }
        public Builder originalFilename(String originalFilename) { this.originalFilename = originalFilename; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public CharacterImageResponse build() {
            CharacterImageResponse r = new CharacterImageResponse();
            r.id = id;
            r.r2Url = r2Url;
            r.r2ObjectKey = r2ObjectKey;
            r.imageType = imageType;
            r.isPrimary = isPrimary;
            r.originalFilename = originalFilename;
            r.fileSizeBytes = fileSizeBytes;
            r.createdAt = createdAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getR2Url() { return r2Url; }
    public void setR2Url(String r2Url) { this.r2Url = r2Url; }
    public String getR2ObjectKey() { return r2ObjectKey; }
    public void setR2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; }
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
