package com.aivideo.storyboard.repository;

import com.aivideo.storyboard.entity.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SceneRepository extends JpaRepository<Scene, UUID> {
    List<Scene> findByStoryboardIdOrderByOrderIndexAsc(UUID storyboardId);
    Optional<Scene> findByIdAndStoryboardId(UUID id, UUID storyboardId);
    long countByStoryboardId(UUID storyboardId);
}
