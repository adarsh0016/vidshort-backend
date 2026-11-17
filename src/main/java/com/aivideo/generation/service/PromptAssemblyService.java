package com.aivideo.generation.service;

import com.aivideo.character.entity.Character;
import com.aivideo.project.entity.Project;
import com.aivideo.storyboard.entity.Scene;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PromptAssemblyService {

    public String assemblePrompt(Project project, Scene scene, List<Character> characters) {
        StringBuilder sb = new StringBuilder();

        // 1. Style / Master Prompt
        if (StringUtils.hasText(project.getMasterPrompt())) {
            sb.append("[STYLE]: ").append(project.getMasterPrompt().trim()).append("\n");
        }

        // 2. Character Descriptions
        if (characters != null && !characters.isEmpty()) {
            sb.append("[CHARACTERS]: ");
            for (Character c : characters) {
                sb.append(c.getName()).append(" (");
                if (StringUtils.hasText(c.getAppearance())) sb.append("appearance: ").append(c.getAppearance()).append("; ");
                if (StringUtils.hasText(c.getClothing())) sb.append("clothing: ").append(c.getClothing()).append("; ");
                if (StringUtils.hasText(c.getPersonality())) sb.append("personality: ").append(c.getPersonality());
                sb.append("). ");
            }
            sb.append("\n");
        }

        // 3. Camera & Shot Settings
        sb.append("[CAMERA & SHOT]: ")
                .append(scene.getShotType()).append(" shot, ")
                .append(scene.getCameraMovement()).append(" camera motion.\n");

        // 4. Scene Action / Prompt
        sb.append("[SCENE]: ").append(scene.getPrompt().trim()).append("\n");

        // 5. Audio Cues
        StringBuilder audio = new StringBuilder();
        if (StringUtils.hasText(scene.getDialogue())) {
            audio.append("Dialogue: \"").append(scene.getDialogue().trim()).append("\" ");
        }
        if (StringUtils.hasText(scene.getSfx())) {
            audio.append("SFX: ").append(scene.getSfx().trim()).append(" ");
        }
        if (StringUtils.hasText(scene.getAmbientAudio())) {
            audio.append("Ambient: ").append(scene.getAmbientAudio().trim());
        }
        if (audio.length() > 0) {
            sb.append("[AUDIO]: ").append(audio.toString().trim()).append("\n");
        }

        // 6. Aspect Ratio & Duration
        sb.append("[SETTINGS]: ").append(scene.getDurationSeconds()).append(" seconds duration, ")
                .append(project.getAspectRatio()).append(" aspect ratio.");

        return sb.toString();
    }
}
