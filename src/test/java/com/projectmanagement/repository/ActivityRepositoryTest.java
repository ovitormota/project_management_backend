package com.projectmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.Client;
import com.projectmanagement.domain.enumeration.Status;

@DataJpaTest
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveActivity() {
        // Creating and saving a client (required for Project due to foreign key constraint)
        Client client = new Client();
        client.setName("Client A");
        client.setEmail("clienta@example.com");
        client.setPhone("987654321");
        clientRepository.save(client);
        entityManager.flush();

        // Creating and saving a project
        Project project = new Project();
        project.setName("Project Alpha");
        project.setDescription("A new software project");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusMonths(1));
        project.setClient(client);
        projectRepository.save(project);
        entityManager.flush();

        // Creating and saving an activity
        Activity activity = new Activity();
        activity.setDescription("Develop module X");
        activity.setStatus(Status.IN_PROGRESS);
        activity.setStartDate(LocalDate.now());
        activity.setEndDate(LocalDate.now().plusDays(10));
        activity.setProject(project);

        activityRepository.save(activity);
        entityManager.flush();

        // Retrieving all activities and validating the saved data
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(1);
        assertThat(activities.get(0).getDescription()).isEqualTo("Develop module X");
        assertThat(activities.get(0).getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(activities.get(0).getProject().getName()).isEqualTo("Project Alpha");
        assertThat(activities.get(0).getProject().getClient().getName()).isEqualTo("Client A");
    }
}
