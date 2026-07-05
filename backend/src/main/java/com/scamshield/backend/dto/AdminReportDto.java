package com.scamshield.backend.dto;

import java.time.LocalDateTime;

public record AdminReportDto(
        Long id,
        Long userId,
        String contentText,
        String category,
        String status,
        LocalDateTime reportedAt
) {}
