package com.scamshield.backend.dto;

import java.util.List;

public record HistoryResponse(
        List<HistoryItemDto> content,
        int totalPages,
        long totalElements,
        int currentPage
) {}
