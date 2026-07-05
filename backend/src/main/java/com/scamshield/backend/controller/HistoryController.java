package com.scamshield.backend.controller;

import com.scamshield.backend.dto.HistoryItemDto;
import com.scamshield.backend.dto.HistoryResponse;
import com.scamshield.backend.entity.ScanHistory;
import com.scamshield.backend.entity.User;
import com.scamshield.backend.exception.ApiException;
import com.scamshield.backend.repository.ScanHistoryRepository;
import com.scamshield.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryController {

    private final ScanHistoryRepository historyRepo;
    private final UserRepository        userRepo;

    public HistoryController(ScanHistoryRepository historyRepo, UserRepository userRepo) {
        this.historyRepo = historyRepo;
        this.userRepo    = userRepo;
    }

    /**
     * GET /api/history?page=0&size=10
     *
     * Returns only the authenticated user's own records — scoped by userId from JWT,
     * never by a client-supplied userId parameter.
     */
    @GetMapping("/history")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        User user = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        Page<ScanHistory> resultPage =
                historyRepo.findByUserIdOrderByScannedAtDesc(user.getId(), pageable);

        List<HistoryItemDto> items = resultPage.getContent().stream()
                .map(h -> new HistoryItemDto(
                        h.getId(),
                        h.getContentType(),
                        h.getContentText(),
                        h.getRiskScore(),
                        h.getExplanation(),
                        h.getScannedAt()
                ))
                .toList();

        return ResponseEntity.ok(new HistoryResponse(
                items,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber()
        ));
    }
}
