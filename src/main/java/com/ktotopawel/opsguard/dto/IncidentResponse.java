package com.ktotopawel.opsguard.dto;

import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.entity.Status;
import com.ktotopawel.opsguard.entity.Tag;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record IncidentResponse(Long id, String description, Severity severity, Status status, Long reporterId, String reporterUsername, LocalDateTime createdAt, Set<String> tagNames) {

    public static IncidentResponse from(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getReportedBy().getId(),
                incident.getReportedBy().getUsername(),
                incident.getCreatedAt(),
                incident.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
        );
    }
}
