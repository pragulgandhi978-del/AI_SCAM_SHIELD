package com.scamshield.backend.controller;

import com.scamshield.backend.dto.*;
import com.scamshield.backend.entity.ScanHistory;
import com.scamshield.backend.entity.User;
import com.scamshield.backend.exception.ApiException;
import com.scamshield.backend.repository.ScanHistoryRepository;
import com.scamshield.backend.repository.UserRepository;
import com.scamshield.backend.service.ExternalApiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ScanController {

    private final ExternalApiService   externalApiService;
    private final ScanHistoryRepository historyRepo;
    private final UserRepository        userRepo;

    public ScanController(ExternalApiService externalApiService,
                          ScanHistoryRepository historyRepo,
                          UserRepository userRepo) {
        this.externalApiService = externalApiService;
        this.historyRepo        = historyRepo;
        this.userRepo           = userRepo;
    }

    // ── POST /api/scan-message ────────────────────────────────────────────

    @PostMapping("/scan-message")
    public ResponseEntity<ScanMessageResponse> scanMessage(
            @Valid @RequestBody ScanMessageRequest req,
            Principal principal) {

        User user = resolveUser(principal);
        externalApiService.checkRateLimit(user.getId());

        ScanMessageResponse result = externalApiService.analyzeMessage(req.text());

        // Persist to scan_history
        historyRepo.save(ScanHistory.builder()
                .userId(user.getId())
                .contentType("MESSAGE")
                .contentText(req.text())
                .riskScore(result.riskLevel())
                .explanation(result.explanation())
                .build());

        return ResponseEntity.ok(result);
    }

    // ── POST /api/scan-url ────────────────────────────────────────────────

    @PostMapping("/scan-url")
    public ResponseEntity<ScanUrlResponse> scanUrl(
            @Valid @RequestBody ScanUrlRequest req,
            Principal principal) {

        User user = resolveUser(principal);
        externalApiService.checkRateLimit(user.getId());

        ScanUrlResponse result = externalApiService.checkUrl(req.url());

        // Persist to scan_history
        historyRepo.save(ScanHistory.builder()
                .userId(user.getId())
                .contentType("URL")
                .contentText(req.url())
                .riskScore(result.status())
                .explanation(null)
                .build());

        return ResponseEntity.ok(result);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private User resolveUser(Principal principal) {
        return userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
