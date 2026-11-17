package com.aivideo.project.controller;

import com.aivideo.common.dto.ApiResponse;
import com.aivideo.project.dto.ProjectRequest;
import com.aivideo.project.dto.ProjectResponse;
import com.aivideo.project.service.ProjectService;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing video generation projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get paginated projects for current user")
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getProjects(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        Page<ProjectResponse> projects = projectService.getProjects(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project details by ID")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        ProjectResponse project = projectService.getProjectById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(project));
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse created = projectService.createProject(user, request);
        return ResponseEntity.ok(ApiResponse.success(created, "Project created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse updated = projectService.updateProject(id, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Project updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Archive/delete a project")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        projectService.deleteProject(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Project deleted successfully"));
    }
}
