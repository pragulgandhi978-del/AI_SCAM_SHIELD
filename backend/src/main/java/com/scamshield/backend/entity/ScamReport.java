package com.scamshield.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scam_reports")
public class ScamReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_text", length = 4000)
    private String contentText;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    // ── Constructors ──────────────────────────────────────────────────────

    public ScamReport() {}

    private ScamReport(Builder b) {
        this.userId      = b.userId;
        this.contentText = b.contentText;
        this.category    = b.category;
        this.status      = b.status != null ? b.status : "PENDING";
        this.reportedAt  = LocalDateTime.now();
    }

    // ── Getters / Setters ─────────────────────────────────────────────────

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public Long getUserId()                          { return userId; }
    public void setUserId(Long userId)               { this.userId = userId; }

    public String getContentText()                   { return contentText; }
    public void setContentText(String contentText)   { this.contentText = contentText; }

    public String getCategory()                      { return category; }
    public void setCategory(String category)         { this.category = category; }

    public String getStatus()                        { return status; }
    public void setStatus(String status)             { this.status = status; }

    public LocalDateTime getReportedAt()             { return reportedAt; }
    public void setReportedAt(LocalDateTime v)       { this.reportedAt = v; }

    // ── Builder ───────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long userId;
        private String contentText;
        private String category;
        private String status;

        public Builder userId(Long userId)           { this.userId = userId;           return this; }
        public Builder contentText(String t)         { this.contentText = t;           return this; }
        public Builder category(String c)            { this.category = c;              return this; }
        public Builder status(String s)              { this.status = s;                return this; }

        public ScamReport build() { return new ScamReport(this); }
    }
}
