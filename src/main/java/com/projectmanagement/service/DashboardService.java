package com.projectmanagement.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.repository.ActivityRepository;
import com.projectmanagement.repository.ProjectRepository;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;

    public DashboardService(ProjectRepository projectRepository, ActivityRepository activityRepository) {
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
    }

    public Map<String, Object> getDashboardIndicators() {
        Map<String, Object> indicators = new HashMap<>();

        List<Status> orderedStatuses = Arrays.asList(Status.PENDING, Status.IN_PROGRESS, Status.OPEN, Status.COMPLETED);

        Map<String, Long> projectStatusCounts = new LinkedHashMap<>();
        for (Status status : orderedStatuses) {
            projectStatusCounts.put(status.name(), projectRepository.countByStatus(status));
        }
        indicators.put("projectStatusCounts", projectStatusCounts);

        Map<String, Long> activityStatusCounts = new LinkedHashMap<>();
        for (Status status : orderedStatuses) {
            activityStatusCounts.put(status.name(), activityRepository.countByStatus(status));
        }
        indicators.put("activityStatusCounts", activityStatusCounts);

        return indicators;
    }
}
