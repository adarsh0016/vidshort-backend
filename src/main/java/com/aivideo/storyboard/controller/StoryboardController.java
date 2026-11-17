package com.aivideo.storyboard.controller;

import com.aivideo.common.dto.ApiResponse;
import com.aivideo.storyboard.dto.*;
import com.aivideo.storyboard.service.StoryboardService;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/storyboard")
@Tag(name = "Storyboard", description = "Endpoints for managing storyboards and scenes")
public class StoryboardController {

    private final StoryboardService storyboardService;

    public StoryboardController(StoryboardService storyboardService) {
        this.storyboardService = storyboardService;
    }

    @GetMapping
    @Operation(summary = "Get or create project storyboard with all scenes")
    public ResponseEntity<ApiResponse<StoryboardResponse>> getStoryboard(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal User user) {
        StoryboardResponse response = storyboardService.getOrCreateStoryboard(projectId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/scenes")
    @Operation(summary = "Add a new scene to the storyboard")
    public ResponseEntity<ApiResponse<SceneResponse>> addScene(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SceneRequest request) {
        SceneResponse response = storyboardService.addScene(projectId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Scene added successfully"));
    }

    @PutMapping("/scenes/{sceneId}")
    @Operation(summary = "Update scene prompt, shot type, audio cues, or characters")
    public ResponseEntity<ApiResponse<SceneResponse>> updateScene(
            @PathVariable UUID projectId,
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SceneRequest request) {
        SceneResponse response = storyboardService.updateScene(sceneId, projectId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Scene updated successfully"));
    }

    @DeleteMapping("/scenes/{sceneId}")
    @Operation(summary = "Delete scene from storyboard")
    public ResponseEntity<ApiResponse<Void>> deleteScene(
            @PathVariable UUID projectId,
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal User user) {
        storyboardService.deleteScene(sceneId, projectId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Scene deleted successfully"));
    }

    @PutMapping("/{storyboardId}/reorder")
    @Operation(summary = "Reorder scenes in storyboard")
    public ResponseEntity<ApiResponse<List<SceneResponse>>> reorderScenes(
            @PathVariable UUID projectId,
            @PathVariable UUID storyboardId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ReorderScenesRequest request) {
        List<SceneResponse> response = storyboardService.reorderScenes(storyboardId, projectId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Scenes reordered successfully"));
    }
}
