package com.aivideo.storyboard.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scenes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"storyboard_id", "order_index"})
})
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyboard_id", nullable = false)
    private Storyboard storyboard;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "shot_type", length = 30)
    private String shotType = "MEDIUM";

    @Column(name = "camera_movement", length = 30)
    private String cameraMovement = "STATIC";

    @Column(columnDefinition = "TEXT")
    private String dialogue;

    @Column(columnDefinition = "TEXT")
    private String sfx;

    @Column(name = "ambient_audio", columnDefinition = "TEXT")
    private String ambientAudio;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds = 8;

    @Column(name = "thumbnail_r2_key", length = 500)
    private String thumbnailR2Key;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "transition_note", columnDefinition = "TEXT")
    private String transitionNote;

    @Column(name = "director_notes", columnDefinition = "TEXT")
    private String directorNotes;

    @OneToMany(mappedBy = "scene", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SceneCharacter> featuredCharacters = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Scene() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Storyboard storyboard;
        private Integer orderIndex;
        private String prompt;
        private String shotType = "MEDIUM";
        private String cameraMovement = "STATIC";
        private String dialogue;
        private String sfx;
        private String ambientAudio;
        private Integer durationSeconds = 8;
        private String thumbnailR2Key;
        private String thumbnailUrl;
        private String transitionNote;
        private String directorNotes;
        private List<SceneCharacter> featuredCharacters = new ArrayList<>();

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder storyboard(Storyboard storyboard) { this.storyboard = storyboard; return this; }
        public Builder orderIndex(Integer orderIndex) { this.orderIndex = orderIndex; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public Builder shotType(String shotType) { this.shotType = shotType; return this; }
        public Builder cameraMovement(String cameraMovement) { this.cameraMovement = cameraMovement; return this; }
        public Builder dialogue(String dialogue) { this.dialogue = dialogue; return this; }
        public Builder sfx(String sfx) { this.sfx = sfx; return this; }
        public Builder ambientAudio(String ambientAudio) { this.ambientAudio = ambientAudio; return this; }
        public Builder durationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public Builder thumbnailR2Key(String thumbnailR2Key) { this.thumbnailR2Key = thumbnailR2Key; return this; }
        public Builder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public Builder transitionNote(String transitionNote) { this.transitionNote = transitionNote; return this; }
        public Builder directorNotes(String directorNotes) { this.directorNotes = directorNotes; return this; }
        public Builder featuredCharacters(List<SceneCharacter> featuredCharacters) { this.featuredCharacters = featuredCharacters; return this; }

        public Scene build() {
            Scene s = new Scene();
            s.setId(id);
            s.setStoryboard(storyboard);
            s.setOrderIndex(orderIndex);
            s.setPrompt(prompt);
            s.setShotType(shotType != null ? shotType : "MEDIUM");
            s.setCameraMovement(cameraMovement != null ? cameraMovement : "STATIC");
            s.setDialogue(dialogue);
            s.setSfx(sfx);
            s.setAmbientAudio(ambientAudio);
            s.setDurationSeconds(durationSeconds != null ? durationSeconds : 8);
            s.setThumbnailR2Key(thumbnailR2Key);
            s.setThumbnailUrl(thumbnailUrl);
            s.setTransitionNote(transitionNote);
            s.setDirectorNotes(directorNotes);
            s.setFeaturedCharacters(featuredCharacters != null ? featuredCharacters : new ArrayList<>());
            return s;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Storyboard getStoryboard() { return storyboard; }
    public void setStoryboard(Storyboard storyboard) { this.storyboard = storyboard; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getShotType() { return shotType; }
    public void setShotType(String shotType) { this.shotType = shotType; }
    public String getCameraMovement() { return cameraMovement; }
    public void setCameraMovement(String cameraMovement) { this.cameraMovement = cameraMovement; }
    public String getDialogue() { return dialogue; }
    public void setDialogue(String dialogue) { this.dialogue = dialogue; }
    public String getSfx() { return sfx; }
    public void setSfx(String sfx) { this.sfx = sfx; }
    public String getAmbientAudio() { return ambientAudio; }
    public void setAmbientAudio(String ambientAudio) { this.ambientAudio = ambientAudio; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getThumbnailR2Key() { return thumbnailR2Key; }
    public void setThumbnailR2Key(String thumbnailR2Key) { this.thumbnailR2Key = thumbnailR2Key; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getTransitionNote() { return transitionNote; }
    public void setTransitionNote(String transitionNote) { this.transitionNote = transitionNote; }
    public String getDirectorNotes() { return directorNotes; }
    public void setDirectorNotes(String directorNotes) { this.directorNotes = directorNotes; }
    public List<SceneCharacter> getFeaturedCharacters() { return featuredCharacters; }
    public void setFeaturedCharacters(List<SceneCharacter> featuredCharacters) { this.featuredCharacters = featuredCharacters; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
