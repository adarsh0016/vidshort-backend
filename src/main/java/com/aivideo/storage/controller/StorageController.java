package com.aivideo.storage.controller;

import com.aivideo.common.dto.ApiResponse;
import com.aivideo.storage.dto.PresignedUrlResponse;
import com.aivideo.storage.dto.StorageStatsResponse;
import com.aivideo.storage.service.R2StorageService;
import com.aivideo.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Storage", description = "Endpoints for Cloudflare R2 uploads, downloads, and usage statistics")
public class StorageController {

    private final R2StorageService storageService;

    public StorageController(R2StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/presigned-upload")
    @Operation(summary = "Get a presigned S3 PUT URL for direct client upload to R2")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUploadUrl(
            @AuthenticationPrincipal User user,
            @RequestParam String filename,
            @RequestParam(defaultValue = "CHARACTER_IMAGE") String category,
            @RequestParam(defaultValue = "image/png") String mimeType) {
        PresignedUrlResponse response = storageService.generatePresignedUploadUrl(user, filename, category, mimeType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get Cloudflare R2 storage usage vs 10GB free tier limit")
    public ResponseEntity<ApiResponse<StorageStatsResponse>> getStorageStats(
            @AuthenticationPrincipal User user) {
        StorageStatsResponse stats = storageService.getStorageStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
