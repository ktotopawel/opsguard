package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
