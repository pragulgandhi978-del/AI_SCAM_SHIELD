package com.scamshield.backend.dto;

/**
 * Returned by /api/register and /api/login.
 * Shape: { "token": "...", "user": { "id", "name", "email", "role" } }
 */
public record AuthResponse(String token, UserDto user) {

    public record UserDto(Long id, String name, String email, String role) {}
}
