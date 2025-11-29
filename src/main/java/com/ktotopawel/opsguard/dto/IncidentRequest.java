package com.ktotopawel.opsguard.dto;

import com.ktotopawel.opsguard.entity.Severity;

import java.util.List;

public record IncidentRequest( String description, Severity severity, List<String> tags) {
}
