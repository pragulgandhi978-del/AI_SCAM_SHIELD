package com.scamshield.backend.controller;

import com.scamshield.backend.dto.DashboardResponse;
import com.scamshield.backend.entity.User;
import com.scamshield.backend.exception.ApiException;
import com.scamshield.backend.repository.ScanHistoryRepository;
import com.scamshield.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final ScanHistoryRepository historyRepo;
    private final UserRepository        userRepo;

    public DashboardController(ScanHistoryRepository historyRepo, UserRepository userRepo) {
        this.historyRepo = historyRepo;
        this.userRepo    = userRepo;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard(Principal principal) {
        User user = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        long totalScans      = historyRepo.countByUserId(user.getId());
        long safeCount       = historyRepo.countSafeByUserId(user.getId());
        long scamCount       = historyRepo.countScamByUserId(user.getId());
        String recentRisk    = historyRepo.findMostRecentRiskScoreByUserId(user.getId());

        return ResponseEntity.ok(new DashboardResponse(totalScans, safeCount, scamCount, recentRisk));
    }
}
