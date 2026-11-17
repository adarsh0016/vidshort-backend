package com.aivideo.generation.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "generation_assets")
public class GenerationAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id", nullable = false)
    private Generation generation;

    @Column(name = "asset_type", nullable = false, length = 30)
    private String assetType;

    @Column(name = "r2_object_key", nullable = false, length = 500)
    private String r2ObjectKey;

    @Column(name = "r2_url", length = 500)
    private String r2Url;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "mime_type", length = 30)
    private String mimeType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public GenerationAsset() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Generation generation;
        private String assetType;
        private String r2ObjectKey;
        private String r2Url;
        private Long fileSizeBytes;
        private String mimeType;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder generation(Generation generation) { this.generation = generation; return this; }
        public Builder assetType(String assetType) { this.assetType = assetType; return this; }
        public Builder r2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; return this; }
        public Builder r2Url(String r2Url) { this.r2Url = r2Url; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder mimeType(String mimeType) { this.mimeType = mimeType; return this; }

        public GenerationAsset build() {
            GenerationAsset a = new GenerationAsset();
            a.setId(id);
            a.setGeneration(generation);
            a.setAssetType(assetType);
            a.setR2ObjectKey(r2ObjectKey);
            a.setR2Url(r2Url);
            a.setFileSizeBytes(fileSizeBytes);
            a.setMimeType(mimeType);
            return a;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Generation getGeneration() { return generation; }
    public void setGeneration(Generation generation) { this.generation = generation; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getR2ObjectKey() { return r2ObjectKey; }
    public void setR2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; }
    public String getR2Url() { return r2Url; }
    public void setR2Url(String r2Url) { this.r2Url = r2Url; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
