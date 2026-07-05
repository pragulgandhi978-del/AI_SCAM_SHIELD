package com.scamshield.backend.controller;

import com.scamshield.backend.dto.ReportScamRequest;
import com.scamshield.backend.entity.ScamReport;
import com.scamshield.backend.entity.User;
import com.scamshield.backend.exception.ApiException;
import com.scamshield.backend.repository.ScamReportRepository;
import com.scamshield.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ScamReportRepository reportRepo;
    private final UserRepository       userRepo;

    public ReportController(ScamReportRepository reportRepo, UserRepository userRepo) {
        this.reportRepo = reportRepo;
        this.userRepo   = userRepo;
    }

    @PostMapping("/report-scam")
    public ResponseEntity<Map<String, String>> reportScam(
            @Valid @RequestBody ReportScamRequest req,
            Principal principal) {

        User user = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        reportRepo.save(ScamReport.builder()
                .userId(user.getId())
                .contentText(req.contentText())
                .category(req.category())
                .status("PENDING")
                .build());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Report submitted successfully"));
    }
}
