package com.aivideo.storyboard.repository;

import com.aivideo.storyboard.entity.SceneCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SceneCharacterRepository extends JpaRepository<SceneCharacter, UUID> {
    List<SceneCharacter> findBySceneId(UUID sceneId);
    void deleteBySceneId(UUID sceneId);
}
