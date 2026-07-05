package com.scamshield.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

/**
 * The spec uses "contentText" but the existing frontend sends "content".
 * @JsonAlias accepts both field names.
 */
public record ReportScamRequest(
        // Accepts "contentText" (spec) or "content" (frontend legacy)
        @JsonAlias("content")
        @NotBlank(message = "Content text is required")
        String contentText,

        @NotBlank(message = "Category is required")
        String category
) {}
