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
public class GeminiVideoService {

    private static final Logger log = LoggerFactory.getLogger(GeminiVideoService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.base-url}")
    private String baseUrl;

    @Value("${gemini.api.default-key:}")
    private String defaultApiKey;

    public byte[] generateVideo(String prompt, String resolution, int durationSeconds,
                                List<String> referenceImageUrls, String lastFrameImageUrl, String apiKey) {
        String keyToUse = (apiKey != null && !apiKey.isBlank()) ? apiKey : defaultApiKey;
        if (keyToUse == null || keyToUse.isBlank()) {
            throw new IllegalStateException("Gemini API key is required");
        }

        String initiateUrl = String.format("%s/v1beta/models/veo-2.0-generate-001:predictLongRunning?key=%s", baseUrl, keyToUse);

        Map<String, Object> instance = new HashMap<>();
        instance.put("prompt", prompt);

        if (referenceImageUrls != null && !referenceImageUrls.isEmpty()) {
            List<Map<String, String>> refImgs = new ArrayList<>();
            for (String url : referenceImageUrls) {
                refImgs.add(Map.of("uri", url));
            }
            instance.put("referenceImages", refImgs);
        }

        if (lastFrameImageUrl != null && !lastFrameImageUrl.isBlank()) {
            instance.put("image", Map.of("uri", lastFrameImageUrl));
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("aspectRatio", "16:9");
        parameters.put("personGeneration", "allow_all");
        parameters.put("durationSeconds", durationSeconds);

        Map<String, Object> requestBody = Map.of(
                "instances", List.of(instance),
                "parameters", parameters
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Sending video generation request to Gemini Veo API...");
            ResponseEntity<String> response = restTemplate.postForEntity(initiateUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String operationName = root.path("name").asText();

                if (operationName != null && !operationName.isBlank()) {
                    return pollVideoOperation(operationName, keyToUse);
                }

                JsonNode predictions = root.path("predictions");
                if (predictions.isArray() && !predictions.isEmpty()) {
                    String videoBase64 = predictions.get(0).path("bytesBase64Encoded").asText();
                    if (!videoBase64.isBlank()) {
                        return Base64.getDecoder().decode(videoBase64);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to generate video with Gemini Veo: {}", e.getMessage(), e);
            throw new RuntimeException("Veo video generation failed: " + e.getMessage(), e);
        }

        throw new RuntimeException("Could not obtain video bytes from Gemini Veo");
    }

    private byte[] pollVideoOperation(String operationName, String apiKey) throws InterruptedException {
        String pollUrl = String.format("%s/v1beta/%s?key=%s", baseUrl, operationName, apiKey);

        int maxAttempts = 60;
        int attempts = 0;

        while (attempts < maxAttempts) {
            Thread.sleep(5000);
            attempts++;

            try {
                ResponseEntity<String> pollRes = restTemplate.getForEntity(pollUrl, String.class);
                if (pollRes.getStatusCode().is2xxSuccessful() && pollRes.getBody() != null) {
                    JsonNode node = objectMapper.readTree(pollRes.getBody());
                    boolean done = node.path("done").asBoolean(false);

                    if (done) {
                        if (node.has("error")) {
                            throw new RuntimeException("Gemini video generation error: " + node.path("error").path("message").asText());
                        }

                        JsonNode responseNode = node.path("response");
                        JsonNode predictions = responseNode.path("predictions");
                        if (predictions.isArray() && !predictions.isEmpty()) {
                            String videoBase64 = predictions.get(0).path("bytesBase64Encoded").asText();
                            if (!videoBase64.isBlank()) {
                                return Base64.getDecoder().decode(videoBase64);
                            }
                            String videoUri = predictions.get(0).path("uri").asText();
                            if (!videoUri.isBlank()) {
                                return restTemplate.getForObject(videoUri, byte[].class);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Polling attempt {} error: {}", attempts, e.getMessage());
            }
        }

        throw new RuntimeException("Video generation timed out after 5 minutes");
    }
}
