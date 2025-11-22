package com.ktotopawel.opsguard.dto;

import com.ktotopawel.opsguard.entity.Severity;

public record IncidentRequest(String description, Severity severity) {}
