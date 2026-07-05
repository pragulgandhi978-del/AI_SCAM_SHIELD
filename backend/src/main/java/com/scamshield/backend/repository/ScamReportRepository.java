package com.scamshield.backend.repository;

import com.scamshield.backend.entity.ScamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScamReportRepository extends JpaRepository<ScamReport, Long> {
    List<ScamReport> findAllByOrderByReportedAtDesc();
}
