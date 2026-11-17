package com.aivideo.storage.repository;

import com.aivideo.storage.entity.R2Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface R2AssetRepository extends JpaRepository<R2Asset, UUID> {
    Optional<R2Asset> findByR2ObjectKey(String r2ObjectKey);
    Page<R2Asset> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    List<R2Asset> findByUserIdAndAssetCategoryAndIsDeletedFalseOrderByCreatedAtDesc(UUID userId, String assetCategory);

    @Query("SELECT COALESCE(SUM(a.fileSizeBytes), 0) FROM R2Asset a WHERE a.user.id = :userId AND a.isDeleted = false")
    long getTotalStorageBytesByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(a) FROM R2Asset a WHERE a.user.id = :userId AND a.isDeleted = false AND a.assetCategory = :category")
    long countByUserIdAndCategory(@Param("userId") UUID userId, @Param("category") String category);
}
