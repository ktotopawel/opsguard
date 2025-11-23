package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Status;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.exception.IncidentNotFoundException;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import com.ktotopawel.opsguard.repository.UserRepository;
import com.ktotopawel.opsguard.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;
    private final UserRepository userRepository;

    public Incident reportIncident(IncidentRequest incidentRequest) {
        Incident incident = new Incident();
        User userProxy = userRepository.getReferenceById(UserContext.get().id());
        incident.setReportedBy(userProxy);
        incident.setDescription(incidentRequest.description());
        incident.setSeverity(incidentRequest.severity());
        return repository.save(incident);
    }

    public List<Incident> getIncidents() {
        return repository.findAll();
    }

    public Incident getIncidentById(Long incidentId) {
        return repository.findById(incidentId).orElseThrow(() -> new IncidentNotFoundException("Incident with id: " + incidentId + " not found"));
    }

    public void deleteIncident(Long incidentId) {
        repository.deleteById(incidentId);
    }

    public Incident closeIncident(Long incidentId) {
        Incident incident = getIncidentById(incidentId);
        incident.setStatus(Status.CLOSED);
        return repository.save(incident);
    }
}
