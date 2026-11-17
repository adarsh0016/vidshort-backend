package com.aivideo.generation.controller;

import com.aivideo.common.dto.ApiResponse;
import com.aivideo.generation.dto.GenerationResponse;
import com.aivideo.generation.service.GenerationService;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/generations")
@Tag(name = "Video Generation", description = "Endpoints for triggering and monitoring Gemini Veo video generations")
public class GenerationController {

    private final GenerationService generationService;

    public GenerationController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping("/scenes/{sceneId}")
    @Operation(summary = "Queue video generation for a specific scene using Gemini Veo API")
    public ResponseEntity<ApiResponse<GenerationResponse>> generateSceneVideo(
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal User user) {
        GenerationResponse response = generationService.queueSceneGeneration(sceneId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "Video generation queued successfully"));
    }

    @PostMapping("/scenes/{sceneId}/thumbnail")
    @Operation(summary = "Generate a storyboard preview thumbnail for a scene using Gemini Imagen")
    public ResponseEntity<ApiResponse<Void>> generateSceneThumbnail(
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal User user) {
        generationService.generateThumbnailForScene(sceneId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Scene thumbnail generated successfully"));
    }

    @GetMapping("/{generationId}/status")
    @Operation(summary = "Check status of a video generation task")
    public ResponseEntity<ApiResponse<GenerationResponse>> getStatus(
            @PathVariable UUID generationId) {
        GenerationResponse response = generationService.getGenerationStatus(generationId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get latest generation status for all scenes in a project")
    public ResponseEntity<ApiResponse<List<GenerationResponse>>> getProjectGenerations(
            @PathVariable UUID projectId) {
        List<GenerationResponse> list = generationService.getLatestGenerationsByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
