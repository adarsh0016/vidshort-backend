package com.aivideo.character.repository;

import com.aivideo.character.entity.CharacterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterImageRepository extends JpaRepository<CharacterImage, UUID> {
    List<CharacterImage> findByCharacterIdOrderByCreatedAtDesc(UUID characterId);
}
