package com.ktotopawel.opsguard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ktotopawel.opsguard.config.PubSubConfig;
import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.dto.IncidentResponse;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.entity.Status;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.exception.IncidentNotFoundException;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import com.ktotopawel.opsguard.repository.UserRepository;
import com.ktotopawel.opsguard.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;
    private final UserRepository userRepository;
    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    public Incident reportIncident(IncidentRequest incidentRequest) {
        Incident incident = new Incident();
        User userProxy = userRepository.getReferenceById(UserContext.get().id());
        incident.setReportedBy(userProxy);
        incident.setDescription(incidentRequest.description());
        incident.setSeverity(incidentRequest.severity());
        Incident savedIncident = repository.save(incident);
        if (savedIncident.getSeverity() == Severity.HIGH || savedIncident.getSeverity() == Severity.CRITICAL)
            publishAlert(incident);
        return savedIncident;
    }

    private void publishAlert(Incident incident) {
        try {
            IncidentResponse payload = IncidentResponse.from(incident);
            String jsonIncident = objectMapper.writeValueAsString(payload);
            pubSubTemplate.publish(PubSubConfig.CRIT_ALERT_TOPIC, jsonIncident);
            log.info("Published incident with id {} to the topic {}", incident.getId(), PubSubConfig.CRIT_ALERT_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Error serializing Incident to JSON", e);
        }
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
