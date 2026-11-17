package com.aivideo.project.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "project_tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "tag"})
})
public class ProjectTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 50)
    private String tag;

    public ProjectTag() {}

    public ProjectTag(UUID id, Project project, String tag) {
        this.id = id;
        this.project = project;
        this.tag = tag;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Project project;
        private String tag;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder project(Project project) { this.project = project; return this; }
        public Builder tag(String tag) { this.tag = tag; return this; }

        public ProjectTag build() {
            return new ProjectTag(id, project, tag);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
