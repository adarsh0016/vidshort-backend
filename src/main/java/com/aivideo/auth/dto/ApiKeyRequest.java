package com.aivideo.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class ApiKeyRequest {
    @NotBlank(message = "API key is required")
    private String apiKey;

    private String provider = "GEMINI";

    public ApiKeyRequest() {}

    public ApiKeyRequest(String apiKey, String provider) {
        this.apiKey = apiKey;
        this.provider = provider != null ? provider : "GEMINI";
    }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
