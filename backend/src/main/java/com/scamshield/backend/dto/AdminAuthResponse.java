package com.scamshield.backend.dto;

/**
 * Returned by /api/admin/login.
 * Shape: { "token": "...", "admin": { "id", "username" } }
 */
public record AdminAuthResponse(String token, AdminDto admin) {

    public record AdminDto(Long id, String username) {}
}
