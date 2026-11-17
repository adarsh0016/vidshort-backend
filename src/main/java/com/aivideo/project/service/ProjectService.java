package com.aivideo.project.service;

import com.aivideo.common.exception.ResourceNotFoundException;
import com.aivideo.project.dto.ProjectRequest;
import com.aivideo.project.dto.ProjectResponse;
import com.aivideo.project.entity.Project;
import com.aivideo.project.entity.ProjectTag;
import com.aivideo.project.repository.ProjectRepository;
import com.aivideo.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(UUID userId, Pageable pageable) {
        log.debug("Fetching paginated projects for user ID [{}]", userId);
        return projectRepository.findByUserIdAndIsArchivedFalseOrderByUpdatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id, UUID userId) {
        log.debug("Fetching project ID [{}] for user ID [{}]", id, userId);
        Project project = projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return mapToResponse(project);
    }

    @Transactional(readOnly = true)
    public Project getEntityById(UUID id, UUID userId) {
        return projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    @Transactional
    public ProjectResponse createProject(User user, ProjectRequest request) {
        log.info("Creating new project with title [{}] for user [{}]", request.getTitle(), user.getId());

        Project project = Project.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .masterPrompt(request.getMasterPrompt())
                .resolution(request.getResolution() != null ? request.getResolution() : "1080p")
                .aspectRatio(request.getAspectRatio() != null ? request.getAspectRatio() : "16:9")
                .genre(request.getGenre())
                .status("DRAFT")
                .isArchived(false)
                .tags(new ArrayList<>())
                .build();

        if (request.getTags() != null) {
            for (String tagStr : request.getTags()) {
                project.getTags().add(ProjectTag.builder()
                        .project(project)
                        .tag(tagStr.trim())
                        .build());
            }
        }

        project = projectRepository.save(project);
        log.info("Project created successfully with ID [{}]", project.getId());
        return mapToResponse(project);
    }

    @Transactional
    public ProjectResponse updateProject(UUID id, UUID userId, ProjectRequest request) {
        log.info("Updating project ID [{}] for user [{}]", id, userId);
        Project project = getEntityById(id, userId);

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setMasterPrompt(request.getMasterPrompt());
        if (request.getResolution() != null) project.setResolution(request.getResolution());
        if (request.getAspectRatio() != null) project.setAspectRatio(request.getAspectRatio());
        project.setGenre(request.getGenre());

        if (request.getTags() != null) {
            project.getTags().clear();
            for (String tagStr : request.getTags()) {
                project.getTags().add(ProjectTag.builder()
                        .project(project)
                        .tag(tagStr.trim())
                        .build());
            }
        }

        project = projectRepository.save(project);
        log.info("Project ID [{}] updated successfully", id);
        return mapToResponse(project);
    }

    @Transactional
    public void deleteProject(UUID id, UUID userId) {
        log.info("Archiving project ID [{}] for user [{}]", id, userId);
        Project project = getEntityById(id, userId);
        project.setIsArchived(true);
        projectRepository.save(project);
        log.info("Project ID [{}] archived successfully", id);
    }

    private ProjectResponse mapToResponse(Project project) {
        List<String> tags = project.getTags() != null
                ? project.getTags().stream().map(ProjectTag::getTag).collect(Collectors.toList())
                : List.of();

        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .masterPrompt(project.getMasterPrompt())
                .resolution(project.getResolution())
                .aspectRatio(project.getAspectRatio())
                .status(project.getStatus())
                .genre(project.getGenre())
                .tags(tags)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
