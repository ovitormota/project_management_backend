package com.projectmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.domain.Client;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.enumeration.Status;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
        Client client = new Client();
        client.setName("Client A");
        client.setEmail("clienta@example.com");
        client.setPhone("987654321");
        clientRepository.save(client);
        entityManager.flush();

        Project project = new Project();
        project.setName("Project Alpha");
        project.setDescription("A new software project");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(Instant.now());
        project.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        project.setClient(client);
        projectRepository.save(project);
        entityManager.flush();

        Activity activity = new Activity();
        activity.setDescription("Develop module X");
        activity.setStatus(Status.IN_PROGRESS);
        activity.setStartDate(Instant.now());
        activity.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        activity.setProject(project);

        activityRepository.save(activity);
        entityManager.flush();

        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(1);
        assertThat(activities.get(0).getDescription()).isEqualTo("Develop module X");
        assertThat(activities.get(0).getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(activities.get(0).getProject().getName()).isEqualTo("Project Alpha");
        assertThat(activities.get(0).getProject().getClient().getName()).isEqualTo("Client A");
    }
}
