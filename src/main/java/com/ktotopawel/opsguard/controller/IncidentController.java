package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.dto.IncidentResponse;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incidents", description = "Manage reports of incidents")
public class IncidentController {
    private final IncidentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Report a new incident", description = "Creates an incident and notifies SREs if Critical")
    public IncidentResponse create(@RequestBody IncidentRequest incidentRequest) {
        return IncidentResponse.from(service.reportIncident(incidentRequest));
    }

    @GetMapping
    @Operation(summary = "List incidents", description = "Returns a list of all incidents")
    public List<IncidentResponse> getAll(@RequestParam(required = false) Set<String> tags, @RequestParam(required = false) Set<Severity> severities) {
        return service.getIncidents(tags, severities).stream().map(IncidentResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get incident by ID", description = "Returns the incident with the id passed as an URLParam")
    public IncidentResponse getById(@PathVariable Long id) {
        return IncidentResponse.from(service.getIncidentById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete an incident", description = "Performs soft delete (tags the incident as deleted in the database, prevents other endpoints from returning it) on the incident with the given ID")
    public void delete(@PathVariable Long id) {
        service.deleteIncident(id);
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Close an incident", description = "Closes an incident, effectively marking it as \"Done\"")
    public IncidentResponse close(@PathVariable Long id) {
        return IncidentResponse.from(service.closeIncident(id));
    }
}
