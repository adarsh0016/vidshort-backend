package com.aivideo.storyboard.entity;

import com.aivideo.project.entity.Project;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "storyboards")
public class Storyboard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20, nullable = false)
    private String status = "DRAFT";

    @Column(name = "total_duration_seconds")
    private Integer totalDurationSeconds = 0;

    @OneToMany(mappedBy = "storyboard", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Scene> scenes = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Storyboard() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Project project;
        private String title;
        private String description;
        private String status = "DRAFT";
        private Integer totalDurationSeconds = 0;
        private List<Scene> scenes = new ArrayList<>();

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder project(Project project) { this.project = project; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalDurationSeconds(Integer totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; return this; }
        public Builder scenes(List<Scene> scenes) { this.scenes = scenes; return this; }

        public Storyboard build() {
            Storyboard s = new Storyboard();
            s.setId(id);
            s.setProject(project);
            s.setTitle(title);
            s.setDescription(description);
            s.setStatus(status != null ? status : "DRAFT");
            s.setTotalDurationSeconds(totalDurationSeconds != null ? totalDurationSeconds : 0);
            s.setScenes(scenes != null ? scenes : new ArrayList<>());
            return s;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalDurationSeconds() { return totalDurationSeconds; }
    public void setTotalDurationSeconds(Integer totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; }
    public List<Scene> getScenes() { return scenes; }
    public void setScenes(List<Scene> scenes) { this.scenes = scenes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
