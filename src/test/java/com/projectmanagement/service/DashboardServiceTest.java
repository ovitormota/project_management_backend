package com.projectmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.repository.ActivityRepository;
import com.projectmanagement.repository.ProjectRepository;

class DashboardServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetDashboardIndicators() {
        when(projectRepository.countByStatus(Status.PENDING)).thenReturn(10L);
        when(projectRepository.countByStatus(Status.IN_PROGRESS)).thenReturn(20L);
        when(projectRepository.countByStatus(Status.OPEN)).thenReturn(5L);
        when(projectRepository.countByStatus(Status.COMPLETED)).thenReturn(15L);

        when(activityRepository.countByStatus(Status.PENDING)).thenReturn(8L);
        when(activityRepository.countByStatus(Status.IN_PROGRESS)).thenReturn(12L);
        when(activityRepository.countByStatus(Status.OPEN)).thenReturn(3L);
        when(activityRepository.countByStatus(Status.COMPLETED)).thenReturn(7L);

        Map<String, Object> indicators = dashboardService.getDashboardIndicators();

        assertNotNull(indicators);
        assertTrue(indicators.containsKey("projectStatusCounts"));
        assertTrue(indicators.containsKey("activityStatusCounts"));

        Map<String, Long> projectStatusCounts = (Map<String, Long>) indicators.get("projectStatusCounts");
        Map<String, Long> activityStatusCounts = (Map<String, Long>) indicators.get("activityStatusCounts");

        assertEquals(10L, projectStatusCounts.get(Status.PENDING.name()));
        assertEquals(20L, projectStatusCounts.get(Status.IN_PROGRESS.name()));
        assertEquals(5L, projectStatusCounts.get(Status.OPEN.name()));
        assertEquals(15L, projectStatusCounts.get(Status.COMPLETED.name()));

        assertEquals(8L, activityStatusCounts.get(Status.PENDING.name()));
        assertEquals(12L, activityStatusCounts.get(Status.IN_PROGRESS.name()));
        assertEquals(3L, activityStatusCounts.get(Status.OPEN.name()));
        assertEquals(7L, activityStatusCounts.get(Status.COMPLETED.name()));

        verify(projectRepository, times(1)).countByStatus(Status.PENDING);
        verify(projectRepository, times(1)).countByStatus(Status.IN_PROGRESS);
        verify(projectRepository, times(1)).countByStatus(Status.OPEN);
        verify(projectRepository, times(1)).countByStatus(Status.COMPLETED);

        verify(activityRepository, times(1)).countByStatus(Status.PENDING);
        verify(activityRepository, times(1)).countByStatus(Status.IN_PROGRESS);
        verify(activityRepository, times(1)).countByStatus(Status.OPEN);
        verify(activityRepository, times(1)).countByStatus(Status.COMPLETED);
    }
}
