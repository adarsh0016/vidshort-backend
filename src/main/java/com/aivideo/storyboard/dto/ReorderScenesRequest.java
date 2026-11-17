package com.aivideo.storyboard.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public class ReorderScenesRequest {
    @NotEmpty(message = "Scene IDs are required in order")
    private List<UUID> sceneIds;

    public ReorderScenesRequest() {}

    public List<UUID> getSceneIds() { return sceneIds; }
    public void setSceneIds(List<UUID> sceneIds) { this.sceneIds = sceneIds; }
}
