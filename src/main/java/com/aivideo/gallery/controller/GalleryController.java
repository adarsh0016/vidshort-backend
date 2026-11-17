package com.aivideo.gallery.controller;

import com.aivideo.common.dto.ApiResponse;
import com.aivideo.storage.entity.R2Asset;
import com.aivideo.storage.repository.R2AssetRepository;
import com.aivideo.storage.service.R2StorageService;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/gallery")
@Tag(name = "Gallery", description = "Endpoints for viewing and managing generated videos and images")
public class GalleryController {

    private final R2AssetRepository r2AssetRepository;
    private final R2StorageService r2StorageService;

    public GalleryController(R2AssetRepository r2AssetRepository, R2StorageService r2StorageService) {
        this.r2AssetRepository = r2AssetRepository;
        this.r2StorageService = r2StorageService;
    }

    @GetMapping
    @Operation(summary = "Get paginated gallery assets (videos and images) for current user")
    public ResponseEntity<ApiResponse<Page<R2Asset>>> getGalleryAssets(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        Page<R2Asset> assets = r2AssetRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(assets));
    }

    @GetMapping("/download/{assetId}")
    @Operation(summary = "Get a fresh presigned download URL for an asset")
    public ResponseEntity<ApiResponse<String>> getDownloadUrl(
            @PathVariable java.util.UUID assetId,
            @AuthenticationPrincipal User user) {
        R2Asset asset = r2AssetRepository.findById(assetId)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new com.aivideo.common.exception.ResourceNotFoundException("Asset", "id", assetId));

        String url = r2StorageService.generatePresignedDownloadUrl(asset.getR2ObjectKey(), Duration.ofHours(24));
        return ResponseEntity.ok(ApiResponse.success(url));
    }
}
