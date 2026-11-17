package com.aivideo.character.entity;

import com.aivideo.project.entity.Project;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String appearance;

    @Column(columnDefinition = "TEXT")
    private String clothing;

    @Column(columnDefinition = "TEXT")
    private String personality;

    @Column(name = "age_range", length = 20)
    private String ageRange;

    @Column(length = 20)
    private String gender;

    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterImage> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Character() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Project project;
        private String name;
        private String appearance;
        private String clothing;
        private String personality;
        private String ageRange;
        private String gender;
        private String additionalNotes;
        private Integer sortOrder = 0;
        private List<CharacterImage> images = new ArrayList<>();

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder project(Project project) { this.project = project; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder appearance(String appearance) { this.appearance = appearance; return this; }
        public Builder clothing(String clothing) { this.clothing = clothing; return this; }
        public Builder personality(String personality) { this.personality = personality; return this; }
        public Builder ageRange(String ageRange) { this.ageRange = ageRange; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder additionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; return this; }
        public Builder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }
        public Builder images(List<CharacterImage> images) { this.images = images; return this; }

        public Character build() {
            Character c = new Character();
            c.setId(id);
            c.setProject(project);
            c.setName(name);
            c.setAppearance(appearance);
            c.setClothing(clothing);
            c.setPersonality(personality);
            c.setAgeRange(ageRange);
            c.setGender(gender);
            c.setAdditionalNotes(additionalNotes);
            c.setSortOrder(sortOrder != null ? sortOrder : 0);
            c.setImages(images != null ? images : new ArrayList<>());
            return c;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAppearance() { return appearance; }
    public void setAppearance(String appearance) { this.appearance = appearance; }
    public String getClothing() { return clothing; }
    public void setClothing(String clothing) { this.clothing = clothing; }
    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }
    public String getAgeRange() { return ageRange; }
    public void setAgeRange(String ageRange) { this.ageRange = ageRange; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public List<CharacterImage> getImages() { return images; }
    public void setImages(List<CharacterImage> images) { this.images = images; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
