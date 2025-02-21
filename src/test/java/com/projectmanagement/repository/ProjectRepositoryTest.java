package com.projectmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.projectmanagement.domain.Client;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.enumeration.Status;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveProject() {
        Client client = new Client();
        client.setName("Client A");
        client.setEmail("clienta@example.com");
        client.setPhone("987654321");
        clientRepository.save(client);
        entityManager.flush();

        Project project = new Project();
        project.setName("Project Beta");
        project.setDescription("A new beta project");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(Instant.now());
        project.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        project.setClient(client);

        projectRepository.save(project);
        entityManager.flush();

        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getName()).isEqualTo("Project Beta");
        assertThat(projects.get(0).getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(projects.get(0).getClient().getName()).isEqualTo("Client A");
    }
}
