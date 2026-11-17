package com.aivideo.generation.dto;

import java.time.Instant;
import java.util.UUID;

public class GenerationResponse {
    private UUID id;
    private UUID sceneId;
    private int sceneOrderIndex;
    private String status;
    private String composedPrompt;
    private String modelUsed;
    private String resolution;
    private Integer durationSeconds;
    private int attemptNumber;
    private String videoUrl;
    private Long fileSizeBytes;
    private Integer apiLatencyMs;
    private String errorMessage;
    private Instant queuedAt;
    private Instant startedAt;
    private Instant completedAt;

    public GenerationResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private UUID sceneId;
        private int sceneOrderIndex;
        private String status;
        private String composedPrompt;
        private String modelUsed;
        private String resolution;
        private Integer durationSeconds;
        private int attemptNumber;
        private String videoUrl;
        private Long fileSizeBytes;
        private Integer apiLatencyMs;
        private String errorMessage;
        private Instant queuedAt;
        private Instant startedAt;
        private Instant completedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder sceneId(UUID sceneId) { this.sceneId = sceneId; return this; }
        public Builder sceneOrderIndex(int sceneOrderIndex) { this.sceneOrderIndex = sceneOrderIndex; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder composedPrompt(String composedPrompt) { this.composedPrompt = composedPrompt; return this; }
        public Builder modelUsed(String modelUsed) { this.modelUsed = modelUsed; return this; }
        public Builder resolution(String resolution) { this.resolution = resolution; return this; }
        public Builder durationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public Builder attemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; return this; }
        public Builder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder apiLatencyMs(Integer apiLatencyMs) { this.apiLatencyMs = apiLatencyMs; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder queuedAt(Instant queuedAt) { this.queuedAt = queuedAt; return this; }
        public Builder startedAt(Instant startedAt) { this.startedAt = startedAt; return this; }
        public Builder completedAt(Instant completedAt) { this.completedAt = completedAt; return this; }

        public GenerationResponse build() {
            GenerationResponse r = new GenerationResponse();
            r.id = id;
            r.sceneId = sceneId;
            r.sceneOrderIndex = sceneOrderIndex;
            r.status = status;
            r.composedPrompt = composedPrompt;
            r.modelUsed = modelUsed;
            r.resolution = resolution;
            r.durationSeconds = durationSeconds;
            r.attemptNumber = attemptNumber;
            r.videoUrl = videoUrl;
            r.fileSizeBytes = fileSizeBytes;
            r.apiLatencyMs = apiLatencyMs;
            r.errorMessage = errorMessage;
            r.queuedAt = queuedAt;
            r.startedAt = startedAt;
            r.completedAt = completedAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSceneId() { return sceneId; }
    public void setSceneId(UUID sceneId) { this.sceneId = sceneId; }
    public int getSceneOrderIndex() { return sceneOrderIndex; }
    public void setSceneOrderIndex(int sceneOrderIndex) { this.sceneOrderIndex = sceneOrderIndex; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComposedPrompt() { return composedPrompt; }
    public void setComposedPrompt(String composedPrompt) { this.composedPrompt = composedPrompt; }
    public String getModelUsed() { return modelUsed; }
    public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public Integer getApiLatencyMs() { return apiLatencyMs; }
    public void setApiLatencyMs(Integer apiLatencyMs) { this.apiLatencyMs = apiLatencyMs; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Instant getQueuedAt() { return queuedAt; }
    public void setQueuedAt(Instant queuedAt) { this.queuedAt = queuedAt; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
