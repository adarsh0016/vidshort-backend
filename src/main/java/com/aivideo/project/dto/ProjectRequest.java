package com.aivideo.project.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ProjectRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private String masterPrompt;
    private String resolution = "1080p";
    private String aspectRatio = "16:9";
    private String genre;
    private List<String> tags;

    public ProjectRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMasterPrompt() { return masterPrompt; }
    public void setMasterPrompt(String masterPrompt) { this.masterPrompt = masterPrompt; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getAspectRatio() { return aspectRatio; }
    public void setAspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
