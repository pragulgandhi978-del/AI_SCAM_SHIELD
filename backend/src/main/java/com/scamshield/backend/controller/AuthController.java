package com.scamshield.backend.controller;

import com.scamshield.backend.dto.*;
import com.scamshield.backend.entity.Admin;
import com.scamshield.backend.entity.User;
import com.scamshield.backend.exception.ApiException;
import com.scamshield.backend.repository.AdminRepository;
import com.scamshield.backend.repository.UserRepository;
import com.scamshield.backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository  userRepo;
    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public AuthController(UserRepository userRepo,
                          AdminRepository adminRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepo        = userRepo;
        this.adminRepo       = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    // ── POST /api/register ───────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role("USER")
                .build();
        user = userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        AuthResponse.UserDto userDto =
                new AuthResponse.UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, userDto));
    }

    // ── POST /api/login ──────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        User user = userRepo.findByEmail(req.email())
                .orElseThrow(() ->
                    new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        AuthResponse.UserDto userDto =
                new AuthResponse.UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }

    // ── POST /api/admin/login ────────────────────────────────────────────

    @PostMapping("/admin/login")
    public ResponseEntity<AdminAuthResponse> adminLogin(@Valid @RequestBody AdminLoginRequest req) {
        Admin admin = adminRepo.findByUsername(req.username())
                .orElseThrow(() ->
                    new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(req.password(), admin.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        // Role embedded in JWT as ADMIN
        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        AdminAuthResponse.AdminDto adminDto =
                new AdminAuthResponse.AdminDto(admin.getId(), admin.getUsername());

        return ResponseEntity.ok(new AdminAuthResponse(token, adminDto));
    }
}
