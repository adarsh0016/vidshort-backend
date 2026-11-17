package com.aivideo.generation.service;

import com.aivideo.auth.service.AuthService;
import com.aivideo.character.entity.Character;
import com.aivideo.character.entity.CharacterImage;
import com.aivideo.common.exception.ResourceNotFoundException;
import com.aivideo.generation.dto.GenerationResponse;
import com.aivideo.generation.entity.Generation;
import com.aivideo.generation.repository.GenerationRepository;
import com.aivideo.project.entity.Project;
import com.aivideo.project.service.ProjectService;
import com.aivideo.storage.entity.R2Asset;
import com.aivideo.storage.service.R2StorageService;
import com.aivideo.storyboard.entity.Scene;
import com.aivideo.storyboard.entity.SceneCharacter;
import com.aivideo.storyboard.repository.SceneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class GenerationService {

    private static final Logger log = LoggerFactory.getLogger(GenerationService.class);

    private final GenerationRepository generationRepository;
    private final SceneRepository sceneRepository;
    private final ProjectService projectService;
    private final PromptAssemblyService promptAssemblyService;
    private final GeminiVideoService geminiVideoService;
    private final GeminiImageService geminiImageService;
    private final VideoMotionSynthesizer videoMotionSynthesizer;
    private final R2StorageService r2StorageService;
    private final AuthService authService;

    public GenerationService(GenerationRepository generationRepository,
                             SceneRepository sceneRepository,
                             ProjectService projectService,
                             PromptAssemblyService promptAssemblyService,
                             GeminiVideoService geminiVideoService,
                             GeminiImageService geminiImageService,
                             VideoMotionSynthesizer videoMotionSynthesizer,
                             R2StorageService r2StorageService,
                             AuthService authService) {
        this.generationRepository = generationRepository;
        this.sceneRepository = sceneRepository;
        this.projectService = projectService;
        this.promptAssemblyService = promptAssemblyService;
        this.geminiVideoService = geminiVideoService;
        this.geminiImageService = geminiImageService;
        this.videoMotionSynthesizer = videoMotionSynthesizer;
        this.r2StorageService = r2StorageService;
        this.authService = authService;
    }

    @Transactional
    public GenerationResponse queueSceneGeneration(UUID sceneId, UUID userId) {
        log.info("Queueing scene generation for scene ID [{}] by user [{}]", sceneId, userId);

        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene", "id", sceneId));

        Project project = scene.getStoryboard().getProject();

        List<Character> characters = scene.getFeaturedCharacters().stream()
                .map(SceneCharacter::getCharacter)
                .collect(Collectors.toList());

        String composedPrompt = promptAssemblyService.assemblePrompt(project, scene, characters);
        log.debug("Composed prompt for scene ID [{}]:\n{}", sceneId, composedPrompt);

        int nextAttempt = generationRepository.findBySceneIdOrderByQueuedAtDesc(sceneId).size() + 1;

        Generation generation = Generation.builder()
                .scene(scene)
                .user(project.getUser())
                .status("QUEUED")
                .composedPrompt(composedPrompt)
                .modelUsed("veo-3")
                .resolution(project.getResolution())
                .durationSeconds(scene.getDurationSeconds())
                .attemptNumber(nextAttempt)
                .queuedAt(Instant.now())
                .build();

        generation = generationRepository.save(generation);
        log.info("Generation job created with ID [{}] (attempt #{}), status [QUEUED]", generation.getId(), nextAttempt);

        processGenerationAsync(generation.getId(), userId);

        return mapToResponse(generation);
    }

    @Async("videoGenExecutor")
    public CompletableFuture<Void> processGenerationAsync(UUID generationId, UUID userId) {
        Generation generation = generationRepository.findById(generationId).orElse(null);
        if (generation == null) {
            log.error("Async worker failed: generation ID [{}] not found", generationId);
            return CompletableFuture.completedFuture(null);
        }

        long startTime = System.currentTimeMillis();
        generation.setStatus("PROCESSING");
        generation.setStartedAt(Instant.now());
        generationRepository.save(generation);
        log.info("Generation [{}] status transitioned to [PROCESSING]", generationId);

        try {
            Scene scene = generation.getScene();
            Project project = scene.getStoryboard().getProject();

            List<String> refImageUrls = new ArrayList<>();
            for (SceneCharacter sc : scene.getFeaturedCharacters()) {
                for (CharacterImage img : sc.getCharacter().getImages()) {
                    if (img.getR2Url() != null) {
                        refImageUrls.add(img.getR2Url());
                    }
                    if (refImageUrls.size() >= 3) break;
                }
                if (refImageUrls.size() >= 3) break;
            }
            log.info("Generation [{}] attached [{}] character reference image(s)", generationId, refImageUrls.size());

            String lastFrameUrl = null;
            if (scene.getOrderIndex() > 0) {
                List<Scene> prevScenes = sceneRepository.findByStoryboardIdOrderByOrderIndexAsc(scene.getStoryboard().getId());
                if (scene.getOrderIndex() - 1 < prevScenes.size()) {
                    Scene prevScene = prevScenes.get(scene.getOrderIndex() - 1);
                    Optional<Generation> prevGen = generationRepository.findFirstBySceneIdAndStatusOrderByCompletedAtDesc(prevScene.getId(), "COMPLETED");
                    if (prevGen.isPresent() && prevGen.get().getLastFrameR2Key() != null) {
                        lastFrameUrl = r2StorageService.generatePresignedDownloadUrl(prevGen.get().getLastFrameR2Key(), Duration.ofHours(1));
                        log.info("Generation [{}] linked previous scene last-frame for continuity from scene [{}]",
                                generationId, prevScene.getId());
                    }
                }
            }

            String geminiApiKey = authService.getDecryptedApiKey(userId, "GEMINI");

            byte[] videoBytes = null;
            try {
                log.info("Generation [{}] attempting Gemini Veo API...", generationId);
                videoBytes = geminiVideoService.generateVideo(
                        generation.getComposedPrompt(),
                        project.getResolution(),
                        scene.getDurationSeconds(),
                        refImageUrls,
                        lastFrameUrl,
                        geminiApiKey
                );
                log.info("Generation [{}] received [{}] video bytes from Gemini Veo", generationId, videoBytes.length);
            } catch (Exception veoEx) {
                log.warn("Generation [{}] Veo API unavailable on key ({}), activating Imagen 3 Motion Video Engine fallback...",
                        generationId, veoEx.getMessage());

                // 1. Generate high-resolution visual with Imagen 3 (supported on standard Gemini API keys)
                byte[] imageBytes = geminiImageService.generateImage(generation.getComposedPrompt(), geminiApiKey);

                // 2. Synthesize smooth camera motion MP4 video
                videoBytes = videoMotionSynthesizer.synthesizeMotionVideo(
                        imageBytes,
                        scene.getCameraMovement(),
                        scene.getDurationSeconds(),
                        project.getResolution()
                );

                generation.setModelUsed("imagen-3-motion");
                generationRepository.save(generation);
                log.info("Generation [{}] successfully rendered [{}] motion video bytes via Imagen 3 Engine",
                        generationId, videoBytes.length);
            }

            generation.setStatus("UPLOADING");
            generationRepository.save(generation);
            log.info("Generation [{}] status transitioned to [UPLOADING]", generationId);

            String videoKey = String.format("users/%s/videos/%s.mp4", userId, generation.getId());
            String videoUrl = r2StorageService.uploadBytes(videoKey, videoBytes, "video/mp4");

            r2StorageService.registerAsset(
                    project.getUser(),
                    videoKey,
                    "VIDEO",
                    "scene-" + scene.getOrderIndex() + ".mp4",
                    videoBytes.length,
                    "video/mp4",
                    generation.getId(),
                    "GENERATION"
            );

            generation.setStatus("COMPLETED");
            generation.setVideoR2Key(videoKey);
            generation.setVideoUrl(videoUrl);
            generation.setFileSizeBytes((long) videoBytes.length);
            generation.setApiLatencyMs((int) (System.currentTimeMillis() - startTime));
            generation.setCompletedAt(Instant.now());
            generationRepository.save(generation);

            log.info("Generation [{}] COMPLETED successfully in [{} ms], file size [{} bytes]",
                    generationId, generation.getApiLatencyMs(), videoBytes.length);

        } catch (Exception e) {
            log.error("Generation [{}] FAILED after [{} ms]: {}",
                    generationId, (System.currentTimeMillis() - startTime), e.getMessage(), e);
            generation.setStatus("FAILED");
            generation.setErrorMessage(e.getMessage());
            generation.setCompletedAt(Instant.now());
            generationRepository.save(generation);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void generateThumbnailForScene(UUID sceneId, UUID userId) {
        log.info("Generating thumbnail for scene ID [{}] by user [{}]", sceneId, userId);
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene", "id", sceneId));

        String prompt = "Cinematic storyboard sketch or still: " + scene.getPrompt();
        String geminiApiKey = authService.getDecryptedApiKey(userId, "GEMINI");

        long start = System.currentTimeMillis();
        byte[] imgBytes = geminiImageService.generateImage(prompt, geminiApiKey);
        log.info("Thumbnail generated from Gemini Imagen in [{} ms] ([{} bytes])",
                System.currentTimeMillis() - start, imgBytes.length);

        String thumbKey = String.format("users/%s/thumbnails/%s.png", userId, scene.getId());
        String thumbUrl = r2StorageService.uploadBytes(thumbKey, imgBytes, "image/png");

        r2StorageService.registerAsset(
                scene.getStoryboard().getProject().getUser(),
                thumbKey,
                "THUMBNAIL",
                "scene-thumb-" + scene.getOrderIndex() + ".png",
                imgBytes.length,
                "image/png",
                scene.getId(),
                "SCENE"
        );

        scene.setThumbnailR2Key(thumbKey);
        scene.setThumbnailUrl(thumbUrl);
        sceneRepository.save(scene);
        log.info("Thumbnail attached to scene [{}] successfully", sceneId);
    }

    @Transactional(readOnly = true)
    public GenerationResponse getGenerationStatus(UUID generationId) {
        Generation gen = generationRepository.findById(generationId)
                .orElseThrow(() -> new ResourceNotFoundException("Generation", "id", generationId));
        return mapToResponse(gen);
    }

    @Transactional(readOnly = true)
    public List<GenerationResponse> getLatestGenerationsByProject(UUID projectId) {
        return generationRepository.findLatestByProjectId(projectId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GenerationResponse mapToResponse(Generation g) {
        return GenerationResponse.builder()
                .id(g.getId())
                .sceneId(g.getScene().getId())
                .sceneOrderIndex(g.getScene().getOrderIndex())
                .status(g.getStatus())
                .composedPrompt(g.getComposedPrompt())
                .modelUsed(g.getModelUsed())
                .resolution(g.getResolution())
                .durationSeconds(g.getDurationSeconds())
                .attemptNumber(g.getAttemptNumber())
                .videoUrl(g.getVideoUrl())
                .fileSizeBytes(g.getFileSizeBytes())
                .apiLatencyMs(g.getApiLatencyMs())
                .errorMessage(g.getErrorMessage())
                .queuedAt(g.getQueuedAt())
                .startedAt(g.getStartedAt())
                .completedAt(g.getCompletedAt())
                .build();
    }
}
