package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.dto.IncidentResponse;
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
    public IncidentResponse create(@RequestBody IncidentRequest incidentRequest) {
        return IncidentResponse.from(service.reportIncident(incidentRequest));
    }

    @GetMapping
    public List<IncidentResponse> getAll() {
        return service.getIncidents().stream().map(IncidentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public IncidentResponse getById(@PathVariable Long id) {
        return IncidentResponse.from(service.getIncidentById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteIncident(id);
    }

    @PatchMapping("/{id}/close")
    public IncidentResponse close(@PathVariable Long id) {
        return IncidentResponse.from(service.closeIncident(id));
    }
}
