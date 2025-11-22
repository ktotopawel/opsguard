package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Status;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;

    public Incident reportIncident(IncidentRequest incidentRequest) {
        Incident incident = new Incident();
        incident.setDescription(incidentRequest.description());
        incident.setSeverity(incidentRequest.severity());
        incident.setStatus(Status.OPEN);
        return repository.save(incident);
    }
}
