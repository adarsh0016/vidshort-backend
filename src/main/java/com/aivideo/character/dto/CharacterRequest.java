package com.aivideo.character.dto;

import jakarta.validation.constraints.NotBlank;

public class CharacterRequest {
    @NotBlank(message = "Character name is required")
    private String name;
    private String appearance;
    private String clothing;
    private String personality;
    private String ageRange;
    private String gender;
    private String additionalNotes;
    private Integer sortOrder;

    public CharacterRequest() {}

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
}
