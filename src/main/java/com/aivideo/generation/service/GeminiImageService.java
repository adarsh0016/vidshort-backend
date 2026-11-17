package com.aivideo.generation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiImageService {

    private static final Logger log = LoggerFactory.getLogger(GeminiImageService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.base-url}")
    private String baseUrl;

    @Value("${gemini.api.default-key:}")
    private String defaultApiKey;

    public byte[] generateImage(String prompt, String apiKey) {
        String keyToUse = (apiKey != null && !apiKey.isBlank()) ? apiKey : defaultApiKey;
        if (keyToUse == null || keyToUse.isBlank()) {
            throw new IllegalStateException("Gemini API key is required");
        }

        String url = String.format("%s/v1beta/models/imagen-3.0-generate-002:predict?key=%s", baseUrl, keyToUse);

        Map<String, Object> instance = Map.of("prompt", prompt);
        Map<String, Object> parameters = Map.of(
                "sampleCount", 1,
                "aspectRatio", "1:1",
                "outputOptions", Map.of("mimeType", "image/png")
        );
        Map<String, Object> requestBody = Map.of(
                "instances", List.of(instance),
                "parameters", parameters
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode predictions = root.path("predictions");
                if (predictions.isArray() && !predictions.isEmpty()) {
                    String base64Bytes = predictions.get(0).path("bytesBase64Encoded").asText();
                    return Base64.getDecoder().decode(base64Bytes);
                }
            }
        } catch (Exception e) {
            log.error("Failed to call Gemini Imagen API: {}", e.getMessage(), e);
            throw new RuntimeException("Gemini Imagen generation failed: " + e.getMessage(), e);
        }

        throw new RuntimeException("No image predictions returned from Gemini Imagen API");
    }
}
