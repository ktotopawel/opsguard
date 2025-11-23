package com.ktotopawel.opsguard.dto;

import com.ktotopawel.opsguard.entity.Severity;

public record IncidentRequest(Long reporterId, String description, Severity severity) {}
