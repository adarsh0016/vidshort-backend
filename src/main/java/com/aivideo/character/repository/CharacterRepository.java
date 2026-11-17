package com.aivideo.character.repository;

import com.aivideo.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
    List<Character> findByProjectIdOrderBySortOrderAsc(UUID projectId);
    Optional<Character> findByIdAndProjectId(UUID id, UUID projectId);
    long countByProjectId(UUID projectId);
}
