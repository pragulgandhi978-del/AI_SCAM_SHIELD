package com.scamshield.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scamshield.backend.dto.ScanMessageResponse;
import com.scamshield.backend.dto.ScanUrlResponse;
import com.scamshield.backend.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    // ── Rate limiter state ────────────────────────────────────────────────
    private static final int    RATE_LIMIT_MAX       = 10;
    private static final long   RATE_LIMIT_WINDOW_MS = 3_600_000L; // 1 hour

    /** userId -> list of request timestamps within the current window */
    private final ConcurrentHashMap<Long, List<Long>> rateLimitMap = new ConcurrentHashMap<>();

    // ── Config ────────────────────────────────────────────────────────────
    private final String       openAiApiKey;
    private final String       safeBrowsingApiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExternalApiService(
            @Value("${openai.api-key}") String openAiApiKey,
            @Value("${safebrowsing.api-key}") String safeBrowsingApiKey,
            @Value("${external.api.connect-timeout-ms:10000}") int connectTimeoutMs,
            @Value("${external.api.read-timeout-ms:10000}") int readTimeoutMs) {

        this.openAiApiKey      = openAiApiKey;
        this.safeBrowsingApiKey = safeBrowsingApiKey;
        this.objectMapper      = new ObjectMapper();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMs);
        factory.setReadTimeout(readTimeoutMs);
        this.restTemplate = new RestTemplate(factory);
    }

    // ── Rate limiting ─────────────────────────────────────────────────────

    /**
     * Checks and records a request for the given userId.
     * Throws 429 ApiException if the rolling-hour limit is exceeded.
     */
    public void checkRateLimit(Long userId) {
        long now = Instant.now().toEpochMilli();
        long windowStart = now - RATE_LIMIT_WINDOW_MS;

        rateLimitMap.compute(userId, (id, timestamps) -> {
            if (timestamps == null) timestamps = new ArrayList<>();
            // Remove timestamps outside the rolling window
            timestamps.removeIf(ts -> ts < windowStart);
            if (timestamps.size() >= RATE_LIMIT_MAX) {
                throw new ApiException(
                    org.springframework.http.HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded, try again later");
            }
            timestamps.add(now);
            return timestamps;
        });
    }

    // ── OpenAI ────────────────────────────────────────────────────────────

    /**
     * Calls OpenAI gpt-4o-mini to classify a message as Low/Medium/High scam risk.
     * Returns ScanMessageResponse with riskLevel and explanation.
     * On any failure, throws 503 ApiException.
     */
    public ScanMessageResponse analyzeMessage(String text) {
        try {
            String systemPrompt =
                "You are a scam detection assistant. Analyze the user's message and classify its scam risk. " +
                "Respond ONLY with valid JSON in this exact format: " +
                "{\"riskLevel\": \"Low\", \"explanation\": \"brief reason\"}. " +
                "riskLevel must be exactly one of: Low, Medium, High. No other text outside the JSON.";

            Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user",   "content", text)
                ),
                "temperature", 0.2,
                "max_tokens", 150
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                String.class
            );

            String body = response.getBody();
            JsonNode root = objectMapper.readTree(body);
            String content = root
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

            // Parse the JSON the model returned
            JsonNode result = objectMapper.readTree(content.trim());
            String riskLevel   = normalizeRiskLevel(result.path("riskLevel").asText("Medium"));
            String explanation = result.path("explanation").asText("No explanation provided.");

            return new ScanMessageResponse(riskLevel, explanation);

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("OpenAI analyzeMessage failed: {}", e.getMessage());
            throw new ApiException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Scan service temporarily unavailable, please try again");
        }
    }

    // ── Google Safe Browsing ──────────────────────────────────────────────

    /**
     * Calls Google Safe Browsing API v4 to check a URL.
     * Returns "Safe" or "Malicious".
     * On any failure, throws 503 ApiException.
     */
    public ScanUrlResponse checkUrl(String url) {
        try {
            String apiUrl = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + safeBrowsingApiKey;

            Map<String, Object> requestBody = Map.of(
                "client", Map.of(
                    "clientId",      "scamshield",
                    "clientVersion", "1.0"
                ),
                "threatInfo", Map.of(
                    "threatTypes",      List.of(
                        "MALWARE", "SOCIAL_ENGINEERING",
                        "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"
                    ),
                    "platformTypes",    List.of("ANY_PLATFORM"),
                    "threatEntryTypes", List.of("URL"),
                    "threatEntries",    List.of(Map.of("url", url))
                )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            String body = response.getBody();
            JsonNode root = objectMapper.readTree(body);

            // If "matches" key is present and non-empty -> Malicious
            JsonNode matches = root.path("matches");
            boolean malicious = matches.isArray() && matches.size() > 0;

            return new ScanUrlResponse(malicious ? "Malicious" : "Safe");

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("SafeBrowsing checkUrl failed: {}", e.getMessage());
            throw new ApiException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                "Scan service temporarily unavailable, please try again");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /**
     * Normalizes a riskLevel string to exactly Low, Medium, or High.
     * Anything unrecognized defaults to Medium.
     */
    private String normalizeRiskLevel(String raw) {
        if (raw == null) return "Medium";
        return switch (raw.trim().toLowerCase()) {
            case "low"    -> "Low";
            case "high"   -> "High";
            case "medium" -> "Medium";
            default       -> "Medium";
        };
    }
}
