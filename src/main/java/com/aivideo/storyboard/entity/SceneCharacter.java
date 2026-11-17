package com.aivideo.storyboard.entity;

import com.aivideo.character.entity.Character;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "scene_characters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"scene_id", "character_id"})
})
public class SceneCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scene_id", nullable = false)
    private Scene scene;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "scene_specific_notes", columnDefinition = "TEXT")
    private String sceneSpecificNotes;

    public SceneCharacter() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private Scene scene;
        private Character character;
        private String sceneSpecificNotes;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder scene(Scene scene) { this.scene = scene; return this; }
        public Builder character(Character character) { this.character = character; return this; }
        public Builder sceneSpecificNotes(String sceneSpecificNotes) { this.sceneSpecificNotes = sceneSpecificNotes; return this; }

        public SceneCharacter build() {
            SceneCharacter sc = new SceneCharacter();
            sc.setId(id);
            sc.setScene(scene);
            sc.setCharacter(character);
            sc.setSceneSpecificNotes(sceneSpecificNotes);
            return sc;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Scene getScene() { return scene; }
    public void setScene(Scene scene) { this.scene = scene; }
    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }
    public String getSceneSpecificNotes() { return sceneSpecificNotes; }
    public void setSceneSpecificNotes(String sceneSpecificNotes) { this.sceneSpecificNotes = sceneSpecificNotes; }
}
