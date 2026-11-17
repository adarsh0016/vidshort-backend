package com.aivideo.project.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProjectResponse {
    private UUID id;
    private String title;
    private String description;
    private String masterPrompt;
    private String resolution;
    private String aspectRatio;
    private String status;
    private String genre;
    private List<String> tags;
    private int characterCount;
    private int sceneCount;
    private Instant createdAt;
    private Instant updatedAt;

    public ProjectResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String title;
        private String description;
        private String masterPrompt;
        private String resolution;
        private String aspectRatio;
        private String status;
        private String genre;
        private List<String> tags;
        private int characterCount;
        private int sceneCount;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder masterPrompt(String masterPrompt) { this.masterPrompt = masterPrompt; return this; }
        public Builder resolution(String resolution) { this.resolution = resolution; return this; }
        public Builder aspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder genre(String genre) { this.genre = genre; return this; }
        public Builder tags(List<String> tags) { this.tags = tags; return this; }
        public Builder characterCount(int characterCount) { this.characterCount = characterCount; return this; }
        public Builder sceneCount(int sceneCount) { this.sceneCount = sceneCount; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public ProjectResponse build() {
            ProjectResponse r = new ProjectResponse();
            r.id = id;
            r.title = title;
            r.description = description;
            r.masterPrompt = masterPrompt;
            r.resolution = resolution;
            r.aspectRatio = aspectRatio;
            r.status = status;
            r.genre = genre;
            r.tags = tags;
            r.characterCount = characterCount;
            r.sceneCount = sceneCount;
            r.createdAt = createdAt;
            r.updatedAt = updatedAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMasterPrompt() { return masterPrompt; }
    public void setMasterPrompt(String masterPrompt) { this.masterPrompt = masterPrompt; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getAspectRatio() { return aspectRatio; }
    public void setAspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public int getCharacterCount() { return characterCount; }
    public void setCharacterCount(int characterCount) { this.characterCount = characterCount; }
    public int getSceneCount() { return sceneCount; }
    public void setSceneCount(int sceneCount) { this.sceneCount = sceneCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
