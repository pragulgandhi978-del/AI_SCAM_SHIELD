package com.scamshield.backend.controller;

import com.scamshield.backend.dto.AdminReportDto;
import com.scamshield.backend.dto.AdminUserDto;
import com.scamshield.backend.repository.ScamReportRepository;
import com.scamshield.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository       userRepo;
    private final ScamReportRepository reportRepo;

    public AdminController(UserRepository userRepo, ScamReportRepository reportRepo) {
        this.userRepo   = userRepo;
        this.reportRepo = reportRepo;
    }

    // ── GET /api/admin/users ──────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<AdminUserDto> users = userRepo.findAll().stream()
                .map(u -> new AdminUserDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getCreatedAt()
                ))
                .toList();
        return ResponseEntity.ok(users);
    }

    // ── GET /api/admin/reports ────────────────────────────────────────────

    @GetMapping("/reports")
    public ResponseEntity<List<AdminReportDto>> getAllReports() {
        List<AdminReportDto> reports = reportRepo.findAllByOrderByReportedAtDesc().stream()
                .map(r -> new AdminReportDto(
                        r.getId(),
                        r.getUserId(),
                        r.getContentText(),
                        r.getCategory(),
                        r.getStatus(),
                        r.getReportedAt()
                ))
                .toList();
        return ResponseEntity.ok(reports);
    }
}
