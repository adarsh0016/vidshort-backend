package com.aivideo.storyboard.dto;

import jakarta.validation.constraints.NotBlank;

public class SplitPromptRequest {
    @NotBlank(message = "Narrative text is required to split into scenes")
    private String narrativeText;

    public SplitPromptRequest() {}

    public String getNarrativeText() { return narrativeText; }
    public void setNarrativeText(String narrativeText) { this.narrativeText = narrativeText; }
}
