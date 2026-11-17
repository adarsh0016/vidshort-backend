package com.aivideo.character.controller;

import com.aivideo.character.dto.CharacterRequest;
import com.aivideo.character.dto.CharacterResponse;
import com.aivideo.character.service.CharacterService;
import com.aivideo.common.dto.ApiResponse;
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
@RequestMapping("/api/projects/{projectId}/characters")
@Tag(name = "Characters", description = "Endpoints for managing characters and reference images")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    @Operation(summary = "List all characters in project")
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getCharacters(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal User user) {
        List<CharacterResponse> characters = characterService.getCharactersByProject(projectId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(characters));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get character detail by ID")
    public ResponseEntity<ApiResponse<CharacterResponse>> getCharacterById(
            @PathVariable UUID projectId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        CharacterResponse character = characterService.getCharacterById(id, projectId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(character));
    }

    @PostMapping
    @Operation(summary = "Create a character for a project")
    public ResponseEntity<ApiResponse<CharacterResponse>> createCharacter(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CharacterRequest request) {
        CharacterResponse created = characterService.createCharacter(projectId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(created, "Character created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update character details")
    public ResponseEntity<ApiResponse<CharacterResponse>> updateCharacter(
            @PathVariable UUID projectId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CharacterRequest request) {
        CharacterResponse updated = characterService.updateCharacter(id, projectId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Character updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete character from project")
    public ResponseEntity<ApiResponse<Void>> deleteCharacter(
            @PathVariable UUID projectId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        characterService.deleteCharacter(id, projectId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Character deleted successfully"));
    }
}
