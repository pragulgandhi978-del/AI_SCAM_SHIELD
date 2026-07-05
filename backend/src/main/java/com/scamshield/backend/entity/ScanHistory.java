package com.scamshield.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scan_history")
public class ScanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_type", nullable = false, length = 20)
    private String contentType;

    @Column(name = "content_text", length = 4000)
    private String contentText;

    @Column(name = "risk_score", length = 20)
    private String riskScore;

    @Column(length = 2000)
    private String explanation;

    @Column(name = "scanned_at", nullable = false)
    private LocalDateTime scannedAt = LocalDateTime.now();

    // ── Constructors ──────────────────────────────────────────────────────

    public ScanHistory() {}

    private ScanHistory(Builder b) {
        this.userId      = b.userId;
        this.contentType = b.contentType;
        this.contentText = b.contentText;
        this.riskScore   = b.riskScore;
        this.explanation = b.explanation;
        this.scannedAt   = LocalDateTime.now();
    }

    // ── Getters / Setters ─────────────────────────────────────────────────

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public Long getUserId()                          { return userId; }
    public void setUserId(Long userId)               { this.userId = userId; }

    public String getContentType()                   { return contentType; }
    public void setContentType(String contentType)   { this.contentType = contentType; }

    public String getContentText()                   { return contentText; }
    public void setContentText(String contentText)   { this.contentText = contentText; }

    public String getRiskScore()                     { return riskScore; }
    public void setRiskScore(String riskScore)       { this.riskScore = riskScore; }

    public String getExplanation()                   { return explanation; }
    public void setExplanation(String explanation)   { this.explanation = explanation; }

    public LocalDateTime getScannedAt()              { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt){ this.scannedAt = scannedAt; }

    // ── Builder ───────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long userId;
        private String contentType;
        private String contentText;
        private String riskScore;
        private String explanation;

        public Builder userId(Long userId)           { this.userId = userId;           return this; }
        public Builder contentType(String t)         { this.contentType = t;           return this; }
        public Builder contentText(String t)         { this.contentText = t;           return this; }
        public Builder riskScore(String r)           { this.riskScore = r;             return this; }
        public Builder explanation(String e)         { this.explanation = e;           return this; }

        public ScanHistory build() { return new ScanHistory(this); }
    }
}
