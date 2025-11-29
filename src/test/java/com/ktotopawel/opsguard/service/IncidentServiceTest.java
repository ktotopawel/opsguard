package com.ktotopawel.opsguard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ktotopawel.opsguard.config.PubSubConfig;
import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.Incident;
import com.ktotopawel.opsguard.entity.Severity;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import com.ktotopawel.opsguard.repository.UserRepository;
import com.ktotopawel.opsguard.security.UserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {
    @Mock
    private IncidentRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PubSubTemplate pubSubTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private IncidentService incidentService;

    @AfterEach
    public void tearDown() {
        UserContext.clear();
    }

    @Test
    @DisplayName("Report Incident: Should save to DB and NOT publish for low severity Incident")
    void testLowSeverityIncident() {
        Long userId = 1L;
        UserContext.set(userId);

        IncidentRequest request = new IncidentRequest("Minor bug", Severity.LOW, List.of("FRONTEND", "UI"));
        User mockUserProxy = new User();
        mockUserProxy.setId(userId);

        when(userRepository.getReferenceById(userId)).thenReturn(mockUserProxy);
        when(repository.save(any(Incident.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Incident result = incidentService.reportIncident(request);
        assertNotNull(result);
        assertEquals(Severity.LOW, result.getSeverity());
        assertEquals(userId, result.getReportedBy().getId());

        verify(repository, times(1)).save(any(Incident.class));

        verifyNoInteractions(pubSubTemplate);
    }

    @Test
    @DisplayName("Report Incident: Should save to the DB AND publish for critical severity Incident")
    void testCriticalSeverityIncident() throws JsonProcessingException {
        Long userId = 1L;
        UserContext.set(userId);
        User mockUserProxy = new User();
        mockUserProxy.setId(userId);

        IncidentRequest request = new IncidentRequest("Prod servers down", Severity.CRITICAL, List.of("INFRA", "SERVERS"));

        when(userRepository.getReferenceById(userId)).thenReturn(mockUserProxy);
        when(repository.save(any(Incident.class))).thenAnswer(invocationOnMock -> {
            Incident incident = invocationOnMock.getArgument(0);
            incident.setId(500L);
            return incident;
        });
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"fake\":\"json\"}");

        Incident result = incidentService.reportIncident(request);
        assertNotNull(result);
        assertEquals(Severity.CRITICAL, result.getSeverity());
        assertEquals(userId, result.getReportedBy().getId());

        verify(repository, times(1)).save(any(Incident.class));
        verify(pubSubTemplate, times(1)).publish(eq(PubSubConfig.CRIT_ALERT_TOPIC), anyString());
    }
}
