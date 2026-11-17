package com.aivideo.storage.dto;

import java.time.Instant;

public class PresignedUrlResponse {
    private String uploadUrl;
    private String downloadUrl;
    private String objectKey;
    private Instant expiresAt;

    public PresignedUrlResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String uploadUrl;
        private String downloadUrl;
        private String objectKey;
        private Instant expiresAt;

        public Builder uploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; return this; }
        public Builder downloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; return this; }
        public Builder objectKey(String objectKey) { this.objectKey = objectKey; return this; }
        public Builder expiresAt(Instant expiresAt) { this.expiresAt = expiresAt; return this; }

        public PresignedUrlResponse build() {
            PresignedUrlResponse r = new PresignedUrlResponse();
            r.uploadUrl = uploadUrl;
            r.downloadUrl = downloadUrl;
            r.objectKey = objectKey;
            r.expiresAt = expiresAt;
            return r;
        }
    }

    public String getUploadUrl() { return uploadUrl; }
    public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public String getObjectKey() { return objectKey; }
    public void setObjectKey(String objectKey) { this.objectKey = objectKey; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
