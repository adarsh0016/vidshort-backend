package com.aivideo.character.service;

import com.aivideo.character.dto.CharacterImageResponse;
import com.aivideo.character.dto.CharacterRequest;
import com.aivideo.character.dto.CharacterResponse;
import com.aivideo.character.entity.Character;
import com.aivideo.character.entity.CharacterImage;
import com.aivideo.character.repository.CharacterImageRepository;
import com.aivideo.character.repository.CharacterRepository;
import com.aivideo.common.exception.ResourceNotFoundException;
import com.aivideo.project.entity.Project;
import com.aivideo.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private static final Logger log = LoggerFactory.getLogger(CharacterService.class);

    private final CharacterRepository characterRepository;
    private final CharacterImageRepository characterImageRepository;
    private final ProjectService projectService;

    public CharacterService(CharacterRepository characterRepository,
                            CharacterImageRepository characterImageRepository,
                            ProjectService projectService) {
        this.characterRepository = characterRepository;
        this.characterImageRepository = characterImageRepository;
        this.projectService = projectService;
    }

    @Transactional(readOnly = true)
    public List<CharacterResponse> getCharactersByProject(UUID projectId, UUID userId) {
        log.debug("Listing characters for project [{}] by user [{}]", projectId, userId);
        projectService.getEntityById(projectId, userId);
        return characterRepository.findByProjectIdOrderBySortOrderAsc(projectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CharacterResponse getCharacterById(UUID id, UUID projectId, UUID userId) {
        projectService.getEntityById(projectId, userId);
        Character character = characterRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "id", id));
        return mapToResponse(character);
    }

    @Transactional(readOnly = true)
    public Character getEntityById(UUID id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "id", id));
    }

    @Transactional
    public CharacterResponse createCharacter(UUID projectId, UUID userId, CharacterRequest request) {
        log.info("Creating character [{}] in project [{}] for user [{}]", request.getName(), projectId, userId);
        Project project = projectService.getEntityById(projectId, userId);

        int order = request.getSortOrder() != null
                ? request.getSortOrder()
                : (int) characterRepository.countByProjectId(projectId);

        Character character = Character.builder()
                .project(project)
                .name(request.getName())
                .appearance(request.getAppearance())
                .clothing(request.getClothing())
                .personality(request.getPersonality())
                .ageRange(request.getAgeRange())
                .gender(request.getGender())
                .additionalNotes(request.getAdditionalNotes())
                .sortOrder(order)
                .build();

        character = characterRepository.save(character);
        log.info("Character [{}] created with ID [{}] in project [{}]", character.getName(), character.getId(), projectId);
        return mapToResponse(character);
    }

    @Transactional
    public CharacterResponse updateCharacter(UUID id, UUID projectId, UUID userId, CharacterRequest request) {
        log.info("Updating character ID [{}] in project [{}]", id, projectId);
        projectService.getEntityById(projectId, userId);
        Character character = characterRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "id", id));

        character.setName(request.getName());
        character.setAppearance(request.getAppearance());
        character.setClothing(request.getClothing());
        character.setPersonality(request.getPersonality());
        character.setAgeRange(request.getAgeRange());
        character.setGender(request.getGender());
        character.setAdditionalNotes(request.getAdditionalNotes());
        if (request.getSortOrder() != null) character.setSortOrder(request.getSortOrder());

        character = characterRepository.save(character);
        log.info("Character ID [{}] updated successfully", id);
        return mapToResponse(character);
    }

    @Transactional
    public void deleteCharacter(UUID id, UUID projectId, UUID userId) {
        log.info("Deleting character ID [{}] from project [{}]", id, projectId);
        projectService.getEntityById(projectId, userId);
        Character character = characterRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "id", id));
        characterRepository.delete(character);
        log.info("Character ID [{}] deleted successfully", id);
    }

    @Transactional
    public CharacterImageResponse addCharacterImage(UUID characterId, String r2ObjectKey, String r2Url,
                                                    String filename, long fileSize, String mimeType,
                                                    boolean isPrimary, String imageType) {
        log.info("Adding reference image [{}] ({} bytes) to character [{}]", filename, fileSize, characterId);
        Character character = getEntityById(characterId);

        if (isPrimary || character.getImages().isEmpty()) {
            character.getImages().forEach(img -> img.setIsPrimary(false));
            isPrimary = true;
        }

        CharacterImage image = CharacterImage.builder()
                .character(character)
                .r2ObjectKey(r2ObjectKey)
                .r2Url(r2Url)
                .originalFilename(filename)
                .fileSizeBytes(fileSize)
                .mimeType(mimeType)
                .isPrimary(isPrimary)
                .imageType(imageType != null ? imageType : "UPLOADED")
                .build();

        image = characterImageRepository.save(image);
        log.info("Character reference image saved with ID [{}]", image.getId());

        return CharacterImageResponse.builder()
                .id(image.getId())
                .r2Url(image.getR2Url())
                .r2ObjectKey(image.getR2ObjectKey())
                .imageType(image.getImageType())
                .isPrimary(image.getIsPrimary())
                .originalFilename(image.getOriginalFilename())
                .fileSizeBytes(image.getFileSizeBytes())
                .createdAt(image.getCreatedAt())
                .build();
    }

    private CharacterResponse mapToResponse(Character character) {
        List<CharacterImageResponse> images = character.getImages() != null
                ? character.getImages().stream().map(img -> CharacterImageResponse.builder()
                        .id(img.getId())
                        .r2Url(img.getR2Url())
                        .r2ObjectKey(img.getR2ObjectKey())
                        .imageType(img.getImageType())
                        .isPrimary(img.getIsPrimary())
                        .originalFilename(img.getOriginalFilename())
                        .fileSizeBytes(img.getFileSizeBytes())
                        .createdAt(img.getCreatedAt())
                        .build()).collect(Collectors.toList())
                : List.of();

        String primaryUrl = images.stream()
                .filter(CharacterImageResponse::isPrimary)
                .map(CharacterImageResponse::getR2Url)
                .findFirst()
                .orElse(images.isEmpty() ? null : images.get(0).getR2Url());

        return CharacterResponse.builder()
                .id(character.getId())
                .projectId(character.getProject().getId())
                .name(character.getName())
                .appearance(character.getAppearance())
                .clothing(character.getClothing())
                .personality(character.getPersonality())
                .ageRange(character.getAgeRange())
                .gender(character.getGender())
                .additionalNotes(character.getAdditionalNotes())
                .sortOrder(character.getSortOrder())
                .images(images)
                .primaryImageUrl(primaryUrl)
                .createdAt(character.getCreatedAt())
                .updatedAt(character.getUpdatedAt())
                .build();
    }
}
