package com.scamshield.backend.repository;

import com.scamshield.backend.entity.ScanHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {

    Page<ScanHistory> findByUserIdOrderByScannedAtDesc(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    @Query("SELECT COUNT(s) FROM ScanHistory s WHERE s.userId = :userId AND s.riskScore IN ('Low', 'Safe')")
    long countSafeByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM ScanHistory s WHERE s.userId = :userId AND s.riskScore IN ('High', 'Medium', 'Malicious')")
    long countScamByUserId(@Param("userId") Long userId);

    @Query("SELECT s.riskScore FROM ScanHistory s WHERE s.userId = :userId ORDER BY s.scannedAt DESC LIMIT 1")
    String findMostRecentRiskScoreByUserId(@Param("userId") Long userId);
}
