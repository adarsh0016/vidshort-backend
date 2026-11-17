package com.aivideo.project.entity;

import com.aivideo.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "master_prompt", columnDefinition = "TEXT")
    private String masterPrompt;

    @Column(length = 10)
    private String resolution = "1080p";

    @Column(name = "aspect_ratio", length = 10)
    private String aspectRatio = "16:9";

    @Column(length = 20, nullable = false)
    private String status = "DRAFT";

    @Column(length = 50)
    private String genre;

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTag> tags = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Project() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private User user;
        private String title;
        private String description;
        private String masterPrompt;
        private String resolution = "1080p";
        private String aspectRatio = "16:9";
        private String status = "DRAFT";
        private String genre;
        private Boolean isArchived = false;
        private List<ProjectTag> tags = new ArrayList<>();

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder masterPrompt(String masterPrompt) { this.masterPrompt = masterPrompt; return this; }
        public Builder resolution(String resolution) { this.resolution = resolution; return this; }
        public Builder aspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder genre(String genre) { this.genre = genre; return this; }
        public Builder isArchived(Boolean isArchived) { this.isArchived = isArchived; return this; }
        public Builder tags(List<ProjectTag> tags) { this.tags = tags; return this; }

        public Project build() {
            Project p = new Project();
            p.setId(id);
            p.setUser(user);
            p.setTitle(title);
            p.setDescription(description);
            p.setMasterPrompt(masterPrompt);
            p.setResolution(resolution != null ? resolution : "1080p");
            p.setAspectRatio(aspectRatio != null ? aspectRatio : "16:9");
            p.setStatus(status != null ? status : "DRAFT");
            p.setGenre(genre);
            p.setIsArchived(isArchived != null ? isArchived : false);
            p.setTags(tags != null ? tags : new ArrayList<>());
            return p;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean archived) { isArchived = archived; }
    public List<ProjectTag> getTags() { return tags; }
    public void setTags(List<ProjectTag> tags) { this.tags = tags; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
