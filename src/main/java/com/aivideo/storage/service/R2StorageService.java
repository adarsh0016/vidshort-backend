package com.aivideo.storage.service;

import com.aivideo.storage.dto.PresignedUrlResponse;
import com.aivideo.storage.dto.StorageStatsResponse;
import com.aivideo.storage.entity.R2Asset;
import com.aivideo.storage.repository.R2AssetRepository;
import com.aivideo.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class R2StorageService {

    private static final Logger log = LoggerFactory.getLogger(R2StorageService.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final R2AssetRepository r2AssetRepository;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.public-url-prefix:}")
    private String publicUrlPrefix;

    private static final double FREE_TIER_LIMIT_GB = 10.0;
    private static final long FREE_TIER_LIMIT_BYTES = 10L * 1024 * 1024 * 1024; // 10 GB

    public R2StorageService(S3Client s3Client, S3Presigner s3Presigner, R2AssetRepository r2AssetRepository) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.r2AssetRepository = r2AssetRepository;
    }

    public PresignedUrlResponse generatePresignedUploadUrl(User user, String filename, String category, String mimeType) {
        log.info("Generating R2 presigned upload URL for user [{}] category [{}] filename [{}]",
                user.getId(), category, filename);

        String ext = filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
        String objectKey = String.format("users/%s/%s/%s%s", user.getId(), category.toLowerCase(), UUID.randomUUID(), ext);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(mimeType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .putObjectRequest(putRequest)
                .build();

        String uploadUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
        String downloadUrl = generatePresignedDownloadUrl(objectKey, Duration.ofHours(2));

        log.debug("Presigned upload URL created for objectKey [{}]", objectKey);

        return PresignedUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .downloadUrl(downloadUrl)
                .objectKey(objectKey)
                .expiresAt(Instant.now().plus(Duration.ofMinutes(30)))
                .build();
    }

    public String generatePresignedDownloadUrl(String objectKey, Duration duration) {
        log.debug("Generating presigned GET download URL for key [{}] duration [{}]", objectKey, duration);

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public String uploadBytes(String objectKey, byte[] data, String mimeType) {
        log.info("Directly uploading [{}] bytes to R2 bucket [{}] with key [{}] mimeType [{}]",
                data.length, bucketName, objectKey, mimeType);

        long start = System.currentTimeMillis();
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(mimeType)
                .contentLength((long) data.length)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(data));
        long latency = System.currentTimeMillis() - start;
        log.info("Uploaded to R2 successfully in [{} ms], key [{}]", latency, objectKey);

        return generatePresignedDownloadUrl(objectKey, Duration.ofDays(7));
    }

    @Transactional
    public R2Asset registerAsset(User user, String objectKey, String category, String originalFilename,
                                 long sizeBytes, String mimeType, UUID refId, String refType) {
        log.info("Registering R2 asset in ledger for user [{}] key [{}] category [{}] size [{} bytes]",
                user.getId(), objectKey, category, sizeBytes);

        R2Asset asset = R2Asset.builder()
                .user(user)
                .r2ObjectKey(objectKey)
                .assetCategory(category)
                .originalFilename(originalFilename)
                .fileSizeBytes(sizeBytes)
                .mimeType(mimeType)
                .referenceId(refId)
                .referenceType(refType)
                .isDeleted(false)
                .build();

        asset = r2AssetRepository.save(asset);
        log.debug("R2 asset registered with ID [{}]", asset.getId());
        return asset;
    }

    @Transactional(readOnly = true)
    public StorageStatsResponse getStorageStats(UUID userId) {
        long totalBytes = r2AssetRepository.getTotalStorageBytesByUserId(userId);
        long videoCount = r2AssetRepository.countByUserIdAndCategory(userId, "VIDEO");
        long imgCount = r2AssetRepository.countByUserIdAndCategory(userId, "CHARACTER_IMAGE")
                + r2AssetRepository.countByUserIdAndCategory(userId, "THUMBNAIL");

        double totalMb = totalBytes / (1024.0 * 1024.0);
        double totalGb = totalBytes / (1024.0 * 1024.0 * 1024.0);
        double percent = (totalBytes * 100.0) / FREE_TIER_LIMIT_BYTES;

        log.debug("Computed storage stats for user [{}]: totalBytes [{}], videoCount [{}]", userId, totalBytes, videoCount);

        return StorageStatsResponse.builder()
                .totalBytesUsed(totalBytes)
                .totalMegabytesUsed(Math.round(totalMb * 100.0) / 100.0)
                .totalGigabytesUsed(Math.round(totalGb * 1000.0) / 1000.0)
                .freeTierLimitGigabytes(FREE_TIER_LIMIT_GB)
                .percentageUsed(Math.min(100.0, Math.round(percent * 10.0) / 10.0))
                .totalVideosCount(videoCount)
                .totalImagesCount(imgCount)
                .build();
    }
}
