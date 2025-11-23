package com.ktotopawel.opsguard.dto;

import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.entity.Status;

import java.time.LocalDateTime;

public record IncidentResponse(Long id, String description, Severity severity, Status status, Long reporterId, String reporterUsername, LocalDateTime createdAt) {

    public static IncidentResponse from(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getReportedBy().getId(),
                incident.getReportedBy().getUsername(),
                incident.getCreatedAt()
        );
    }
}
