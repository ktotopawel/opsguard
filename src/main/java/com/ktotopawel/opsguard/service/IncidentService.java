package com.ktotopawel.opsguard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ktotopawel.opsguard.config.PubSubConfig;
import com.ktotopawel.opsguard.dto.AssignRequest;
import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.dto.IncidentResponse;
import com.ktotopawel.opsguard.entity.*;
import com.ktotopawel.opsguard.exception.IllegalOperationException;
import com.ktotopawel.opsguard.exception.IncidentNotFoundException;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import com.ktotopawel.opsguard.repository.UserRepository;
import com.ktotopawel.opsguard.security.UserContext;
import com.ktotopawel.opsguard.spec.IncidentSpecification;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;
    private final UserRepository userRepository;
    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;
    private final TagService tagService;
    private final UserService userService;

    @Transactional
    public Incident reportIncident(IncidentRequest incidentRequest) {
        Incident incident = new Incident();
        User userProxy = userRepository.getReferenceById(UserContext.get().id());

        incident.setReportedBy(userProxy);
        incident.setDescription(incidentRequest.description());
        incident.setSeverity(incidentRequest.severity());

        Set<Tag> tags = incidentRequest.tags().stream()
                .map(tagService::findOrCreate)
                .collect(Collectors.toSet());
        incident.setTags(tags);

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

    public List<Incident> getIncidents(Set<String> tags, Set<Severity> severity) {
        Specification<Incident> spec = Specification.unrestricted();
        if (tags != null && !tags.isEmpty()) {
            spec = spec.and(IncidentSpecification.hasTags(tags));
        }
        if (severity != null && !severity.isEmpty()) {
            spec = spec.and(IncidentSpecification.hasSeverities(severity));
        }
        return repository.findAll(spec);
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
        incident.setClosedBy(userRepository.getReferenceById(UserContext.get().id()));
        return repository.save(incident);
    }

    public Incident assignUser(Long incidentId, AssignRequest assignRequest) {
        try {
            Incident incident = getIncidentById(incidentId);
            if (incident.getStatus().equals(Status.CLOSED)) {
                throw new IllegalOperationException("Incident with id: " + incidentId + " is already closed");
            }
            incident.setStatus(Status.IN_PROGRESS);
            User assignee = userService.getUserById(assignRequest.assigneeId());
            incident.setAssignedTo(assignee);
            return repository.save(incident);
        } catch (OptimisticLockException e) {
            throw new IllegalOperationException("Incident was modified by another user.");
        }
    }
}
