package com.aivideo.generation.entity;

import com.aivideo.storyboard.entity.Scene;
import com.aivideo.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "generations")
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scene_id", nullable = false)
    private Scene scene;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String status = "QUEUED";

    @Column(name = "composed_prompt", columnDefinition = "TEXT")
    private String composedPrompt;

    @Column(name = "model_used", length = 30)
    private String modelUsed = "veo-3";

    @Column(length = 10)
    private String resolution;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber = 1;

    @Column(name = "video_r2_key", length = 500)
    private String videoR2Key;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "last_frame_r2_key", length = 500)
    private String lastFrameR2Key;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "mime_type", length = 30)
    private String mimeType = "video/mp4";

    @Column(name = "api_latency_ms")
    private Integer apiLatencyMs;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "generation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenerationAsset> assets = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "queued_at", nullable = false, updatable = false)
    private Instant queuedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    public Generation() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Scene scene;
        private User user;
        private String status = "QUEUED";
        private String composedPrompt;
        private String modelUsed = "veo-3";
        private String resolution;
        private Integer durationSeconds;
        private Integer attemptNumber = 1;
        private String videoR2Key;
        private String videoUrl;
        private String lastFrameR2Key;
        private Long fileSizeBytes;
        private String mimeType = "video/mp4";
        private Integer apiLatencyMs;
        private String errorMessage;
        private List<GenerationAsset> assets = new ArrayList<>();
        private Instant queuedAt;
        private Instant startedAt;
        private Instant completedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder scene(Scene scene) { this.scene = scene; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder composedPrompt(String composedPrompt) { this.composedPrompt = composedPrompt; return this; }
        public Builder modelUsed(String modelUsed) { this.modelUsed = modelUsed; return this; }
        public Builder resolution(String resolution) { this.resolution = resolution; return this; }
        public Builder durationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public Builder attemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; return this; }
        public Builder videoR2Key(String videoR2Key) { this.videoR2Key = videoR2Key; return this; }
        public Builder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public Builder lastFrameR2Key(String lastFrameR2Key) { this.lastFrameR2Key = lastFrameR2Key; return this; }
        public Builder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder mimeType(String mimeType) { this.mimeType = mimeType; return this; }
        public Builder apiLatencyMs(Integer apiLatencyMs) { this.apiLatencyMs = apiLatencyMs; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder assets(List<GenerationAsset> assets) { this.assets = assets; return this; }
        public Builder queuedAt(Instant queuedAt) { this.queuedAt = queuedAt; return this; }
        public Builder startedAt(Instant startedAt) { this.startedAt = startedAt; return this; }
        public Builder completedAt(Instant completedAt) { this.completedAt = completedAt; return this; }

        public Generation build() {
            Generation g = new Generation();
            g.setId(id);
            g.setScene(scene);
            g.setUser(user);
            g.setStatus(status != null ? status : "QUEUED");
            g.setComposedPrompt(composedPrompt);
            g.setModelUsed(modelUsed != null ? modelUsed : "veo-3");
            g.setResolution(resolution);
            g.setDurationSeconds(durationSeconds);
            g.setAttemptNumber(attemptNumber != null ? attemptNumber : 1);
            g.setVideoR2Key(videoR2Key);
            g.setVideoUrl(videoUrl);
            g.setLastFrameR2Key(lastFrameR2Key);
            g.setFileSizeBytes(fileSizeBytes);
            g.setMimeType(mimeType != null ? mimeType : "video/mp4");
            g.setApiLatencyMs(apiLatencyMs);
            g.setErrorMessage(errorMessage);
            g.setAssets(assets != null ? assets : new ArrayList<>());
            g.setQueuedAt(queuedAt);
            g.setStartedAt(startedAt);
            g.setCompletedAt(completedAt);
            return g;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) { this.scene = scene; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }
    public String getVideoR2Key() { return videoR2Key; }
    public void setVideoR2Key(String videoR2Key) { this.videoR2Key = videoR2Key; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getLastFrameR2Key() { return lastFrameR2Key; }
    public void setLastFrameR2Key(String lastFrameR2Key) { this.lastFrameR2Key = lastFrameR2Key; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Integer getApiLatencyMs() { return apiLatencyMs; }
    public void setApiLatencyMs(Integer apiLatencyMs) { this.apiLatencyMs = apiLatencyMs; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public List<GenerationAsset> getAssets() { return assets; }
    public void setAssets(List<GenerationAsset> assets) { this.assets = assets; }
    public Instant getQueuedAt() { return queuedAt; }
    public void setQueuedAt(Instant queuedAt) { this.queuedAt = queuedAt; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
