package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@RequestBody IncidentRequest  incidentRequest) {
        return service.reportIncident(incidentRequest);
    }

    @GetMapping
    public List<Incident> getAll() {
        return service.getIncidents();
    }

    @GetMapping("/{id}")
    public Incident getById(@PathVariable Long id) {
        return service.getIncident(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteIncident(id);
    }

    @PatchMapping("/{id}/close")
    public Incident close(@PathVariable Long id) {
        return service.closeIncident(id);
    }
}
