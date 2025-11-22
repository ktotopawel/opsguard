package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Status;
import com.ktotopawel.opsguard.exception.IncidentNotFoundException;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;

    public Incident reportIncident(IncidentRequest incidentRequest) {
        Incident incident = new Incident();
        incident.setDescription(incidentRequest.description());
        incident.setSeverity(incidentRequest.severity());
        return repository.save(incident);
    }

    public List<Incident> getIncidents() {
        return repository.findAll();
    }

    public Incident getIncident(Long incidentId) {
        return repository.findById(incidentId).orElseThrow(() -> new IncidentNotFoundException("Incident with id: " + incidentId + " not found"));
    }

    public void deleteIncident(Long incidentId) {
        repository.deleteById(incidentId);
    }

    public Incident closeIncident(Long incidentId) {
        Incident incident = getIncident(incidentId);
        incident.setStatus(Status.CLOSED);
        return repository.save(incident);
    }
}
