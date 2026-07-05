package com.scamshield.backend.dto;

import java.time.LocalDateTime;

public record HistoryItemDto(
        Long id,
        String contentType,
        String contentText,
        String riskScore,
        String explanation,
        LocalDateTime scannedAt
) {}
