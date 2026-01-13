package com.ktotopawel.opsguard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ktotopawel.opsguard.config.PubSubConfig;
import com.ktotopawel.opsguard.dto.AssignRequest;
import com.ktotopawel.opsguard.dto.IncidentRequest;
import com.ktotopawel.opsguard.entity.*;
import com.ktotopawel.opsguard.exception.IllegalOperationException;
import com.ktotopawel.opsguard.repository.IncidentRepository;
import com.ktotopawel.opsguard.repository.UserRepository;
import com.ktotopawel.opsguard.security.TestSecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Mock
    private UserService userService;
    @Mock
    private TagService tagService;

    @InjectMocks
    private IncidentService incidentService;

    @AfterEach
    public void tearDown() {
        TestSecurityUtils.clearSecurityContext();
    }

    @Test
    @DisplayName("Report Incident: Should save to DB and NOT publish for low severity Incident")
    void testLowSeverityIncident() {
        Long userId = 1L;
        TestSecurityUtils.setupSecurityContext(userId);

        IncidentRequest request = new IncidentRequest("Minor bug", Severity.LOW, List.of("FRONTEND", "UI"));
        User mockUserProxy = new User();
        mockUserProxy.setId(userId);

        when(userRepository.getReferenceById(userId)).thenReturn(mockUserProxy);

        when(tagService.findOrCreate(anyString())).thenAnswer(i -> {
            Tag t = new Tag();
            t.setName(i.getArgument(0));
            return t;
        });

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
        TestSecurityUtils.setupSecurityContext(userId);
        User mockUserProxy = new User();
        mockUserProxy.setId(userId);

        IncidentRequest request = new IncidentRequest("Prod servers down", Severity.CRITICAL, List.of("INFRA", "SERVERS"));

        when(userRepository.getReferenceById(userId)).thenReturn(mockUserProxy);

        when(tagService.findOrCreate(anyString())).thenReturn(new Tag());

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

    @Test
    @DisplayName("Assign User: Should change the status of the incident, assign the correct user and save the data to the database")
    void testAssignUser() {
        Long userId = 1L;
        TestSecurityUtils.setupSecurityContext(userId);
        User mockUserProxy = new User();
        mockUserProxy.setId(userId);

        Long assignedUserId = 2L;
        User assignedUser = new User();
        assignedUser.setId(assignedUserId);

        Incident incident = new Incident();
        incident.setId(500L);
        incident.setStatus(Status.OPEN);
        incident.setDescription("db is slow");

        when(userService.getUserById(assignedUserId)).thenReturn(assignedUser);
        when(repository.findById(500L)).thenReturn(Optional.of(incident));
        when(repository.save(any(Incident.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Incident res = incidentService.assignUser(incident.getId(), new AssignRequest(assignedUserId));

        assertEquals(Status.IN_PROGRESS, res.getStatus());
        assertEquals(assignedUserId, res.getAssignedTo().getId());
    }

    @Test
    @DisplayName("Assign User: Throws an exception when an incident is already closed")
    void testAssignUserWithAlreadyClosedIncident() {
        Long userId = 1L;
        TestSecurityUtils.setupSecurityContext(userId);

        Incident incident = new Incident();
        incident.setId(500L);
        incident.setStatus(Status.CLOSED);
        incident.setDescription("db is slow");

        when(repository.findById(500L)).thenReturn(Optional.of(incident));

        assertThrows(IllegalOperationException.class, () -> {
            incidentService.assignUser(incident.getId(), new AssignRequest(2L));
        });
        verify(repository, never()).save(any(Incident.class));
    }
}