package com.aivideo.character.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CharacterResponse {
    private UUID id;
    private UUID projectId;
    private String name;
    private String appearance;
    private String clothing;
    private String personality;
    private String ageRange;
    private String gender;
    private String additionalNotes;
    private Integer sortOrder;
    private List<CharacterImageResponse> images;
    private String primaryImageUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public CharacterResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private UUID projectId;
        private String name;
        private String appearance;
        private String clothing;
        private String personality;
        private String ageRange;
        private String gender;
        private String additionalNotes;
        private Integer sortOrder;
        private List<CharacterImageResponse> images;
        private String primaryImageUrl;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder projectId(UUID projectId) { this.projectId = projectId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder appearance(String appearance) { this.appearance = appearance; return this; }
        public Builder clothing(String clothing) { this.clothing = clothing; return this; }
        public Builder personality(String personality) { this.personality = personality; return this; }
        public Builder ageRange(String ageRange) { this.ageRange = ageRange; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder additionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; return this; }
        public Builder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }
        public Builder images(List<CharacterImageResponse> images) { this.images = images; return this; }
        public Builder primaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public CharacterResponse build() {
            CharacterResponse r = new CharacterResponse();
            r.id = id;
            r.projectId = projectId;
            r.name = name;
            r.appearance = appearance;
            r.clothing = clothing;
            r.personality = personality;
            r.ageRange = ageRange;
            r.gender = gender;
            r.additionalNotes = additionalNotes;
            r.sortOrder = sortOrder;
            r.images = images;
            r.primaryImageUrl = primaryImageUrl;
            r.createdAt = createdAt;
            r.updatedAt = updatedAt;
            return r;
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAppearance() { return appearance; }
    public void setAppearance(String appearance) { this.appearance = appearance; }
    public String getClothing() { return clothing; }
    public void setClothing(String clothing) { this.clothing = clothing; }
    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }
    public String getAgeRange() { return ageRange; }
    public void setAgeRange(String ageRange) { this.ageRange = ageRange; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public List<CharacterImageResponse> getImages() { return images; }
    public void setImages(List<CharacterImageResponse> images) { this.images = images; }
    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
