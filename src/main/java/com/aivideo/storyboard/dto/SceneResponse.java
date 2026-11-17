package com.aivideo.storyboard.dto;

import com.aivideo.character.dto.CharacterResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SceneResponse {
    private UUID id;
    private UUID storyboardId;
    private int orderIndex;
    private String prompt;
    private String shotType;
    private String cameraMovement;
    private String dialogue;
    private String sfx;
    private String ambientAudio;
    private int durationSeconds;
    private String thumbnailUrl;
    private String thumbnailR2Key;
    private String transitionNote;
    private String directorNotes;
    private List<CharacterResponse> characters;
    private Instant createdAt;
    private Instant updatedAt;

    public SceneResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private UUID storyboardId;
        private int orderIndex;
        private String prompt;
        private String shotType;
        private String cameraMovement;
        private String dialogue;
        private String sfx;
        private String ambientAudio;
        private int durationSeconds;
        private String thumbnailUrl;
        private String thumbnailR2Key;
        private String transitionNote;
        private String directorNotes;
        private List<CharacterResponse> characters;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder storyboardId(UUID storyboardId) { this.storyboardId = storyboardId; return this; }
        public Builder orderIndex(int orderIndex) { this.orderIndex = orderIndex; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public Builder shotType(String shotType) { this.shotType = shotType; return this; }
        public Builder cameraMovement(String cameraMovement) { this.cameraMovement = cameraMovement; return this; }
        public Builder dialogue(String dialogue) { this.dialogue = dialogue; return this; }
        public Builder sfx(String sfx) { this.sfx = sfx; return this; }
        public Builder ambientAudio(String ambientAudio) { this.ambientAudio = ambientAudio; return this; }
        public Builder durationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public Builder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public Builder thumbnailR2Key(String thumbnailR2Key) { this.thumbnailR2Key = thumbnailR2Key; return this; }
        public Builder transitionNote(String transitionNote) { this.transitionNote = transitionNote; return this; }
        public Builder directorNotes(String directorNotes) { this.directorNotes = directorNotes; return this; }
        public Builder characters(List<CharacterResponse> characters) { this.characters = characters; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public SceneResponse build() {
            SceneResponse r = new SceneResponse();
            r.id = id;
            r.storyboardId = storyboardId;
            r.orderIndex = orderIndex;
            r.prompt = prompt;
            r.shotType = shotType;
            r.cameraMovement = cameraMovement;
            r.dialogue = dialogue;
            r.sfx = sfx;
            r.ambientAudio = ambientAudio;
            r.durationSeconds = durationSeconds;
            r.thumbnailUrl = thumbnailUrl;
            r.thumbnailR2Key = thumbnailR2Key;
            r.transitionNote = transitionNote;
            r.directorNotes = directorNotes;
            r.characters = characters;
            r.createdAt = createdAt;
            r.updatedAt = updatedAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getStoryboardId() { return storyboardId; }
    public void setStoryboardId(UUID storyboardId) { this.storyboardId = storyboardId; }
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
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
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getThumbnailR2Key() { return thumbnailR2Key; }
    public void setThumbnailR2Key(String thumbnailR2Key) { this.thumbnailR2Key = thumbnailR2Key; }
    public String getTransitionNote() { return transitionNote; }
    public void setTransitionNote(String transitionNote) { this.transitionNote = transitionNote; }
    public String getDirectorNotes() { return directorNotes; }
    public void setDirectorNotes(String directorNotes) { this.directorNotes = directorNotes; }
    public List<CharacterResponse> getCharacters() { return characters; }
    public void setCharacters(List<CharacterResponse> characters) { this.characters = characters; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
