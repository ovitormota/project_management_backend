package com.projectmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private Activity activity;

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        activity = new Activity();
        activity.setId(1L);
        activity.setDescription("Test Activity");
        activity.setStatus(Status.IN_PROGRESS);
        activity.setStartDate(LocalDate.now());
        activity.setProject(project);
    }

    @Test
    void shouldSaveActivity() {
        when(activityRepository.save(activity)).thenReturn(activity);

        Activity savedActivity = activityService.save(activity);

        assertThat(savedActivity).isNotNull();
        assertThat(savedActivity.getDescription()).isEqualTo("Test Activity");
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    void shouldUpdateActivity() {
        when(activityRepository.save(activity)).thenReturn(activity);

        Activity updatedActivity = activityService.update(activity);

        assertThat(updatedActivity).isNotNull();
        assertThat(updatedActivity.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    void shouldFindActivityById() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        Optional<Activity> foundActivity = activityService.findOne(1L);

        assertThat(foundActivity).isPresent();
        assertThat(foundActivity.get().getId()).isEqualTo(1L);
        verify(activityRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteActivityById() {
        doNothing().when(activityRepository).deleteById(1L);

        activityService.delete(1L);

        verify(activityRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldCheckIfActivityExists() {
        when(activityRepository.existsById(1L)).thenReturn(true);

        boolean exists = activityService.exists(1L);

        assertThat(exists).isTrue();
        verify(activityRepository, times(1)).existsById(1L);
    }
}
