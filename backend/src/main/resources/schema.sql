-- ── Users ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(255)        NOT NULL UNIQUE,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(20)         NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ── Admins ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS admins (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100)        NOT NULL UNIQUE,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ── Scan History ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS scan_history (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT              NOT NULL,
    content_type  VARCHAR(20)         NOT NULL,   -- 'MESSAGE' | 'URL'
    content_text  VARCHAR(4000),
    risk_score    VARCHAR(20),                     -- 'Low' | 'Medium' | 'High' | 'Safe' | 'Malicious'
    explanation   VARCHAR(2000),
    scanned_at    TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scan_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── Scam Reports ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS scam_reports (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT              NOT NULL,
    content_text  VARCHAR(4000),
    category      VARCHAR(50)         NOT NULL,
    status        VARCHAR(20)         NOT NULL DEFAULT 'PENDING',
    reported_at   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
