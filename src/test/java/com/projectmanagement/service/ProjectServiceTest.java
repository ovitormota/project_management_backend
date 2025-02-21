package com.projectmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projectmanagement.domain.Client;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(Instant.now());
        project.setClient(client);
    }

    @Test
    void shouldSaveProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.save(project);

        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getName()).isEqualTo("Test Project");
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldUpdateProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Project updatedProject = projectService.update(project);

        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldFindProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectService.findOne(1L);

        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getId()).isEqualTo(1L);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteProjectById() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.delete(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldCheckIfProjectExists() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        boolean exists = projectService.exists(1L);

        assertThat(exists).isTrue();
        verify(projectRepository, times(1)).existsById(1L);
    }
}
