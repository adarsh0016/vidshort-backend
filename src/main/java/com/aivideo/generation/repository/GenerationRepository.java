package com.aivideo.generation.repository;

import com.aivideo.generation.entity.Generation;
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
public interface GenerationRepository extends JpaRepository<Generation, UUID> {
    List<Generation> findBySceneIdOrderByQueuedAtDesc(UUID sceneId);
    Optional<Generation> findFirstBySceneIdAndStatusOrderByCompletedAtDesc(UUID sceneId, String status);
    Page<Generation> findByUserIdOrderByQueuedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT g FROM Generation g WHERE g.scene.storyboard.project.id = :projectId ORDER BY g.scene.orderIndex ASC, g.queuedAt DESC")
    List<Generation> findLatestByProjectId(@Param("projectId") UUID projectId);
}
