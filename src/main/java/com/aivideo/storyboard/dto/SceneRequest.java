package com.aivideo.storyboard.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class SceneRequest {
    @NotBlank(message = "Scene prompt is required")
    private String prompt;
    private String shotType = "MEDIUM";
    private String cameraMovement = "STATIC";
    private String dialogue;
    private String sfx;
    private String ambientAudio;
    private Integer durationSeconds = 8;
    private String transitionNote;
    private String directorNotes;
    private List<UUID> characterIds;

    public SceneRequest() {}

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
    public String getTransitionNote() { return transitionNote; }
    public void setTransitionNote(String transitionNote) { this.transitionNote = transitionNote; }
    public String getDirectorNotes() { return directorNotes; }
    public void setDirectorNotes(String directorNotes) { this.directorNotes = directorNotes; }
    public List<UUID> getCharacterIds() { return characterIds; }
    public void setCharacterIds(List<UUID> characterIds) { this.characterIds = characterIds; }
}
