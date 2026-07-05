package com.scamshield.backend.dto;

public record DashboardResponse(
        long totalScans,
        long safeCount,
        long scamCount,
        String recentRiskLevel   // null if no scans yet
) {}
