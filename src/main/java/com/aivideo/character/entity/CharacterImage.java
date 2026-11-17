package com.aivideo.character.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "character_images")
public class CharacterImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "r2_object_key", nullable = false, length = 500)
    private String r2ObjectKey;

    @Column(name = "r2_url", length = 500)
    private String r2Url;

    @Column(name = "image_type", nullable = false, length = 20)
    private String imageType = "UPLOADED";

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "original_filename", length = 100)
    private String originalFilename;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "mime_type", length = 50)
    private String mimeType;

    @Column(name = "width_px")
    private Integer widthPx;

    @Column(name = "height_px")
    private Integer heightPx;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public CharacterImage() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Character character;
        private String r2ObjectKey;
        private String r2Url;
        private String imageType = "UPLOADED";
        private Boolean isPrimary = false;
        private String originalFilename;
        private Long fileSizeBytes;
        private String mimeType;
        private Integer widthPx;
        private Integer heightPx;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder character(Character character) { this.character = character; return this; }
        public Builder r2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; return this; }
        public Builder r2Url(String r2Url) { this.r2Url = r2Url; return this; }
        public Builder imageType(String imageType) { this.imageType = imageType; return this; }
        public Builder isPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; return this; }
        public Builder originalFilename(String originalFilename) { this.originalFilename = originalFilename; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder mimeType(String mimeType) { this.mimeType = mimeType; return this; }
        public Builder widthPx(Integer widthPx) { this.widthPx = widthPx; return this; }
        public Builder heightPx(Integer heightPx) { this.heightPx = heightPx; return this; }

        public CharacterImage build() {
            CharacterImage img = new CharacterImage();
            img.setId(id);
            img.setCharacter(character);
            img.setR2ObjectKey(r2ObjectKey);
            img.setR2Url(r2Url);
            img.setImageType(imageType != null ? imageType : "UPLOADED");
            img.setIsPrimary(isPrimary != null ? isPrimary : false);
            img.setOriginalFilename(originalFilename);
            img.setFileSizeBytes(fileSizeBytes);
            img.setMimeType(mimeType);
            img.setWidthPx(widthPx);
            img.setHeightPx(heightPx);
            return img;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }
    public String getR2ObjectKey() { return r2ObjectKey; }
    public void setR2ObjectKey(String r2ObjectKey) { this.r2ObjectKey = r2ObjectKey; }
    public String getR2Url() { return r2Url; }
    public void setR2Url(String r2Url) { this.r2Url = r2Url; }
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean primary) { isPrimary = primary; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Integer getWidthPx() { return widthPx; }
    public void setWidthPx(Integer widthPx) { this.widthPx = widthPx; }
    public Integer getHeightPx() { return heightPx; }
    public void setHeightPx(Integer heightPx) { this.heightPx = heightPx; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
