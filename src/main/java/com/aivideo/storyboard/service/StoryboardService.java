package com.aivideo.storyboard.service;

import com.aivideo.character.dto.CharacterResponse;
import com.aivideo.character.entity.Character;
import com.aivideo.character.service.CharacterService;
import com.aivideo.common.exception.ResourceNotFoundException;
import com.aivideo.project.entity.Project;
import com.aivideo.project.service.ProjectService;
import com.aivideo.storyboard.dto.*;
import com.aivideo.storyboard.entity.Scene;
import com.aivideo.storyboard.entity.SceneCharacter;
import com.aivideo.storyboard.entity.Storyboard;
import com.aivideo.storyboard.repository.SceneCharacterRepository;
import com.aivideo.storyboard.repository.SceneRepository;
import com.aivideo.storyboard.repository.StoryboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoryboardService {

    private static final Logger log = LoggerFactory.getLogger(StoryboardService.class);

    private final StoryboardRepository storyboardRepository;
    private final SceneRepository sceneRepository;
    private final SceneCharacterRepository sceneCharacterRepository;
    private final ProjectService projectService;
    private final CharacterService characterService;

    public StoryboardService(StoryboardRepository storyboardRepository,
                             SceneRepository sceneRepository,
                             SceneCharacterRepository sceneCharacterRepository,
                             ProjectService projectService,
                             CharacterService characterService) {
        this.storyboardRepository = storyboardRepository;
        this.sceneRepository = sceneRepository;
        this.sceneCharacterRepository = sceneCharacterRepository;
        this.projectService = projectService;
        this.characterService = characterService;
    }

    @Transactional
    public StoryboardResponse getOrCreateStoryboard(UUID projectId, UUID userId) {
        log.debug("Fetching/creating storyboard for project [{}] user [{}]", projectId, userId);
        Project project = projectService.getEntityById(projectId, userId);

        Storyboard storyboard = storyboardRepository.findByProjectId(projectId)
                .orElseGet(() -> {
                    log.info("No storyboard found for project [{}]. Creating initial storyboard.", projectId);
                    Storyboard sb = Storyboard.builder()
                            .project(project)
                            .title(project.getTitle() + " Storyboard")
                            .description("Storyboard for " + project.getTitle())
                            .status("DRAFT")
                            .totalDurationSeconds(0)
                            .scenes(new ArrayList<>())
                            .build();
                    return storyboardRepository.save(sb);
                });

        return mapToResponse(storyboard);
    }

    @Transactional
    public SceneResponse addScene(UUID projectId, UUID userId, SceneRequest request) {
        log.info("Adding new scene to storyboard for project [{}] user [{}]", projectId, userId);
        StoryboardResponse sbRes = getOrCreateStoryboard(projectId, userId);
        Storyboard storyboard = storyboardRepository.findById(sbRes.getId()).orElseThrow();

        int nextOrder = (int) sceneRepository.countByStoryboardId(storyboard.getId());

        Scene scene = Scene.builder()
                .storyboard(storyboard)
                .orderIndex(nextOrder)
                .prompt(request.getPrompt())
                .shotType(request.getShotType() != null ? request.getShotType() : "MEDIUM")
                .cameraMovement(request.getCameraMovement() != null ? request.getCameraMovement() : "STATIC")
                .dialogue(request.getDialogue())
                .sfx(request.getSfx())
                .ambientAudio(request.getAmbientAudio())
                .durationSeconds(request.getDurationSeconds() != null ? request.getDurationSeconds() : 8)
                .transitionNote(request.getTransitionNote())
                .directorNotes(request.getDirectorNotes())
                .featuredCharacters(new ArrayList<>())
                .build();

        scene = sceneRepository.save(scene);
        log.info("Scene added with ID [{}] at order index [{}]", scene.getId(), nextOrder);

        if (request.getCharacterIds() != null) {
            for (UUID charId : request.getCharacterIds()) {
                Character ch = characterService.getEntityById(charId);
                SceneCharacter sc = SceneCharacter.builder()
                        .scene(scene)
                        .character(ch)
                        .build();
                scene.getFeaturedCharacters().add(sc);
                sceneCharacterRepository.save(sc);
            }
            log.info("Attached [{}] character(s) to scene [{}]", request.getCharacterIds().size(), scene.getId());
        }

        recalculateStoryboardDuration(storyboard);
        return mapSceneToResponse(scene);
    }

    @Transactional
    public SceneResponse updateScene(UUID sceneId, UUID projectId, UUID userId, SceneRequest request) {
        log.info("Updating scene ID [{}] in project [{}]", sceneId, projectId);
        projectService.getEntityById(projectId, userId);
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene", "id", sceneId));

        scene.setPrompt(request.getPrompt());
        if (request.getShotType() != null) scene.setShotType(request.getShotType());
        if (request.getCameraMovement() != null) scene.setCameraMovement(request.getCameraMovement());
        scene.setDialogue(request.getDialogue());
        scene.setSfx(request.getSfx());
        scene.setAmbientAudio(request.getAmbientAudio());
        if (request.getDurationSeconds() != null) scene.setDurationSeconds(request.getDurationSeconds());
        scene.setTransitionNote(request.getTransitionNote());
        scene.setDirectorNotes(request.getDirectorNotes());

        if (request.getCharacterIds() != null) {
            sceneCharacterRepository.deleteBySceneId(scene.getId());
            scene.getFeaturedCharacters().clear();
            for (UUID charId : request.getCharacterIds()) {
                Character ch = characterService.getEntityById(charId);
                SceneCharacter sc = SceneCharacter.builder()
                        .scene(scene)
                        .character(ch)
                        .build();
                scene.getFeaturedCharacters().add(sc);
                sceneCharacterRepository.save(sc);
            }
        }

        scene = sceneRepository.save(scene);
        log.info("Scene ID [{}] updated successfully", sceneId);
        recalculateStoryboardDuration(scene.getStoryboard());
        return mapSceneToResponse(scene);
    }

    @Transactional
    public void deleteScene(UUID sceneId, UUID projectId, UUID userId) {
        log.info("Deleting scene ID [{}] from project [{}]", sceneId, projectId);
        projectService.getEntityById(projectId, userId);
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene", "id", sceneId));

        Storyboard sb = scene.getStoryboard();
        sceneRepository.delete(scene);

        List<Scene> remaining = sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(sb.getId());
        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setOrderIndex(i);
        }
        sceneRepository.saveAll(remaining);
        log.info("Scene ID [{}] deleted and remaining [{}] scenes re-indexed", sceneId, remaining.size());
        recalculateStoryboardDuration(sb);
    }

    @Transactional
    public List<SceneResponse> reorderScenes(UUID storyboardId, UUID projectId, UUID userId, ReorderScenesRequest request) {
        log.info("Reordering [{}] scenes in storyboard [{}]", request.getSceneIds().size(), storyboardId);
        projectService.getEntityById(projectId, userId);
        List<UUID> orderedIds = request.getSceneIds();

        List<Scene> scenes = sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(storyboardId);
        for (Scene s : scenes) {
            int newIdx = orderedIds.indexOf(s.getId());
            if (newIdx >= 0) {
                s.setOrderIndex(newIdx);
            }
        }
        scenes = sceneRepository.saveAll(scenes);
        log.info("Storyboard [{}] scenes successfully reordered", storyboardId);
        return sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(storyboardId).stream()
                .map(this::mapSceneToResponse)
                .collect(Collectors.toList());
    }

    private void recalculateStoryboardDuration(Storyboard sb) {
        List<Scene> scenes = sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(sb.getId());
        int total = scenes.stream().mapToInt(Scene::getDurationSeconds).sum();
        sb.setTotalDurationSeconds(total);
        storyboardRepository.save(sb);
        log.debug("Recalculated storyboard [{}] total runtime: [{}s]", sb.getId(), total);
    }

    public StoryboardResponse mapToResponse(Storyboard sb) {
        List<SceneResponse> sceneResponses = sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(sb.getId())
                .stream()
                .map(this::mapSceneToResponse)
                .collect(Collectors.toList());

        return StoryboardResponse.builder()
                .id(sb.getId())
                .projectId(sb.getProject().getId())
                .title(sb.getTitle())
                .description(sb.getDescription())
                .status(sb.getStatus())
                .totalDurationSeconds(sb.getTotalDurationSeconds() != null ? sb.getTotalDurationSeconds() : 0)
                .scenes(sceneResponses)
                .createdAt(sb.getCreatedAt())
                .updatedAt(sb.getUpdatedAt())
                .build();
    }

    public SceneResponse mapSceneToResponse(Scene scene) {
        List<CharacterResponse> chars = scene.getFeaturedCharacters() != null
                ? scene.getFeaturedCharacters().stream()
                .map(sc -> CharacterResponse.builder()
                        .id(sc.getCharacter().getId())
                        .name(sc.getCharacter().getName())
                        .appearance(sc.getCharacter().getAppearance())
                        .clothing(sc.getCharacter().getClothing())
                        .build())
                .collect(Collectors.toList())
                : List.of();

        return SceneResponse.builder()
                .id(scene.getId())
                .storyboardId(scene.getStoryboard().getId())
                .orderIndex(scene.getOrderIndex())
                .prompt(scene.getPrompt())
                .shotType(scene.getShotType())
                .cameraMovement(scene.getCameraMovement())
                .dialogue(scene.getDialogue())
                .sfx(scene.getSfx())
                .ambientAudio(scene.getAmbientAudio())
                .durationSeconds(scene.getDurationSeconds())
                .thumbnailUrl(scene.getThumbnailUrl())
                .thumbnailR2Key(scene.getThumbnailR2Key())
                .transitionNote(scene.getTransitionNote())
                .directorNotes(scene.getDirectorNotes())
                .characters(chars)
                .createdAt(scene.getCreatedAt())
                .updatedAt(scene.getUpdatedAt())
                .build();
    }
}
