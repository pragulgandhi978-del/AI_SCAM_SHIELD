package com.scamshield.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScanMessageRequest(
        @NotBlank(message = "Text is required")
        @Size(max = 2000, message = "Text must be 2000 characters or fewer")
        String text
) {}
