package com.aivideo.storyboard.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class StoryboardResponse {
    private UUID id;
    private UUID projectId;
    private String title;
    private String description;
    private String status;
    private int totalDurationSeconds;
    private List<SceneResponse> scenes;
    private Instant createdAt;
    private Instant updatedAt;

    public StoryboardResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private UUID projectId;
        private String title;
        private String description;
        private String status;
        private int totalDurationSeconds;
        private List<SceneResponse> scenes;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder projectId(UUID projectId) { this.projectId = projectId; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalDurationSeconds(int totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; return this; }
        public Builder scenes(List<SceneResponse> scenes) { this.scenes = scenes; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public StoryboardResponse build() {
            StoryboardResponse r = new StoryboardResponse();
            r.id = id;
            r.projectId = projectId;
            r.title = title;
            r.description = description;
            r.status = status;
            r.totalDurationSeconds = totalDurationSeconds;
            r.scenes = scenes;
            r.createdAt = createdAt;
            r.updatedAt = updatedAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getTotalDurationSeconds() { return totalDurationSeconds; }
    public void setTotalDurationSeconds(int totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; }
    public List<SceneResponse> getScenes() { return scenes; }
    public void setScenes(List<SceneResponse> scenes) { this.scenes = scenes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
